package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.LicenseWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

    //    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static ExecutorService threadPoolLicense = Executors.newFixedThreadPool(Constant.threadsCountLicense);
    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);

    static final Object lockDig = new Object();


    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        LoggerUtil.log("--- VERSION : " + Constant.version + " ---");
        Main main = new Main();
        main.runGame();
        System.out.println((float) (System.currentTimeMillis() - time) / 1000.0f);
    }

    private void runGame() {
        Client client = new Client();
        client.setLicenseWrapperList(new CopyOnWriteArrayList<>());
        client.setMoney(new CopyOnWriteArrayList<>());

        List<CompletableFuture<Void>> cfLicenseList = new ArrayList<>();
        List<CompletableFuture<List<String>>> cfTreasuresList = new ArrayList<>();

        for (int line = 0; line < Constant.mapSize; line++) {
//        получаем все точки с сокровищами для линии line
            CopyOnWriteArraySet<Point> points = Actions.getPoints(line);
            LoggerUtil.log("Line: " + line + "; Points with treasures: " + points.size());

            ConcurrentLinkedQueue<Point> pointStack = new ConcurrentLinkedQueue<>(points);
            pointStack.add(new Point(0, 0, 0, 0));

            //Пока стек с точками не пустой
            while (!pointStack.isEmpty()) {

                //Убираем истекшие лицензии
                client.getLicenseWrapperList().removeIf(item -> item.getLicense() != null
                        && item.getLicense().getId() != null
                        && item.getLicense().getDigUsed() >= item.getLicense().getDigAllowed());

                //Обновляем лицензии
                if (cfLicenseList.size() + client.getLicenseWrapperList().size() < Constant.maxLicencesCount) {
                    CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> Actions.updateLicenses(client), threadPoolLicense);
                    cfLicenseList.add(cf);
                }
                //Если лицензия получена - убираем её из списка
                cfLicenseList.removeIf(CompletableFuture::isDone);

                //копаем
                if (cfTreasuresList.size() < Constant.threadsCountDig) {
                    CompletableFuture<List<String>> cf = CompletableFuture
                            .supplyAsync(() -> Actions.dig(client.getLicenseWrapperList(), pointStack), threadPoolDig)
                            .thenApply(dig -> {
                                List<String> res = null;
                                if (dig != null) {

                                    //выкопать не удалось - лицензию не потратили
                                    if (dig.getResponse() == null) {
                                        synchronized (lockDig) {
                                            client.getLicenseWrapperList().stream()
                                                    .filter(item -> dig.getLicence() != null && item.getLicense().getId().equals(dig.getLicence().getId()))
                                                    .findFirst()
                                                    .ifPresent(licenseWrapper -> licenseWrapper.setUseCount(licenseWrapper.getUseCount() - 1));
                                        }
                                    }
                                    //Если что-то выкопали (может и пустое)
                                    else {
                                        synchronized (lockDig) {
                                            int licId = dig.getLicence().getId();

                                            //Обновляем лицензию в общем списке
                                            client.getLicenseWrapperList().stream()
                                                    .filter(item -> item.getLicense().getId().equals(licId))
                                                    .findFirst()
                                                    .ifPresent(item -> item.getLicense().setDigUsed(item.getLicense().getDigUsed() + 1));
                                        }

                                        //обновляем точку
                                        Point point = dig.getPoint();
                                        point.setDepth(point.getDepth() + 1);
                                        point.setTreasuresCount(point.getTreasuresCount() - dig.getResponse().length);

                                        //Помещаем обратно точку в стек - если сокровища еще есть
                                        if (point.getTreasuresCount() > 0) {
                                            pointStack.add(point);
                                        }

                                        //Сохраняем в коллекцию сокровища
                                        res = Arrays.asList(dig.getResponse());
                                    }
                                }

                                return res;
                            });
                    cfTreasuresList.add(cf);
                }


                Iterator<CompletableFuture<List<String>>> tresIterator = cfTreasuresList.iterator();
                while (tresIterator.hasNext()) {
                    CompletableFuture<List<String>> cf = tresIterator.next();
                    if (cf != null && cf.isDone()) {
                        try {
                            List<String> treasures = cf.get();

                            if (treasures != null && treasures.size() > 0) {
                                CompletableFuture
                                        .runAsync(() -> {
                                            List<Integer> money = Actions.cash(treasures);
                                            if (money != null) client.getMoney().addAll(money);
                                        });
                            }
                            tresIterator.remove();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }


//                        .thenApply(treasures -> {
//                            List<Integer> res = null;
//                            if ( treasures != null && treasures.size() > 0) res = Actions.cash(treasures);
//                            return res;
//                        })
//                        .thenAccept(money -> {
//                            if (money != null) client.getMoney().addAll(money);
//                        });


//                Actions.cashWithoutResponse(treasures);


            }

        }

        LoggerUtil.log("FINISH");

    }

}