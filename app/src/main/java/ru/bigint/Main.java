package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        LoggerUtil.log("--- VERSION : " + Constant.version + " ---");
        Main main = new Main();
        main.runGame();
        System.out.println((float) (System.currentTimeMillis() - time) / 1000.0f);
    }

    private void runGame() {
        Client client = new Client();
        client.setLicenses(new CopyOnWriteArrayList<>());
        client.setMoney(new CopyOnWriteArrayList<>());

        for (int line = 0; line < Constant.mapSize; line++) {
            //получаем все точки с сокровищами для линии line
            CopyOnWriteArraySet<Point> points = Actions.getPoints(line);
            LoggerUtil.log("Line: " + line + "; Points with treasures: " + points.size());

            ConcurrentLinkedQueue<Point> pointStack = new ConcurrentLinkedQueue<>(points);

//            Balance balance = Actions.balance();
//            client.setMoney( new LinkedList<>(Arrays.asList(balance.getWallet())) );

            //Пока стек с точками не пустой
            while (!pointStack.isEmpty()) {

                    //Обновляем лицензии
                    CompletableFuture.runAsync(() -> {
                        Actions.updateLicenses(client);
                    }, threadPool);


                    //копаем
                    CompletableFuture
                            .supplyAsync(() -> Actions.dig(client.getLicenses(), pointStack), threadPool)
                            .thenApply(dig -> {
                                List<String> res = null;
                                //Если что-то выкопали (может и пустое)
                                if (dig != null && dig.getResponse() != null) {
                                    int licId = dig.getLicence().getId();
                                    //Обновляем лицензию в общем списке
                                    License license = client.getLicenses().stream()
                                            .filter(item -> item.getId() == licId)
                                            .findFirst()
                                            .get();
                                    license.setDigUsed(license.getDigUsed() + 1);

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
                                return res;
                            })
                            .thenAccept(treasures -> {
                                List<Integer> money = Actions.cash(treasures);
                                client.getMoney().addAll(money);
                            });


//                    CompletableFuture.runAsync(() -> {
//                        List<Integer> money = Actions.cash(treasures);
//                        client.getMoney().addAll(money);
////                Actions.cashWithoutResponse(treasures);
//                    });


            }

        }

        LoggerUtil.log("FINISH");

    }

}