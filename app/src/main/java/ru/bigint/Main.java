package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {

    private final static int maxDepth = 10;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
//        Logger.log("--- Running App ---");

        long startTime = System.currentTimeMillis();

        Main main = new Main();
        main.runGame();

//        Logger.log("Time: " + (System.currentTimeMillis() - startTime));
    }

    private void runGame() throws IOException, InterruptedException, ExecutionException {
//        Logger.log("--- Play Game ---");
//        Logger.log("OS: " + System.getProperty("os.name"));
//        Logger.log("URI: " + URI);

        int resMoney = 0;

//        RequestEndpoint.healthCheck(URI);

        Client client = new Client();

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = Action.getExplore();

        List<Integer> treasureAmountList = new ArrayList<>(treasureMap.keySet());
        //Копаем сначала в точках с максимальным содержанием сокровищ
        for (int pointTreasureCount = treasureAmountList.size() - 1; pointTreasureCount > 0; pointTreasureCount--) {
            List<Point> points = treasureMap.get(pointTreasureCount);
            if (points != null) {

                //Делим список на части
//                List<List<Point>> pointPartitions = new ArrayList<>();
//                for (int i = 0; i < points.size(); i += Constant.threadsCount) {
//                    pointPartitions.add(points.subList(i, Math.min(i + Constant.threadsCount, points.size())));
//                }

//                for (List<Point> list : pointPartitions) {
//                    treasures = Action.digArea(list);
//                }

                Queue<Point> queue = new LinkedList<>();
                queue.addAll(points);

                while ( !queue.isEmpty() ) {
                    Point point = queue.poll();

                    int treasuresCount = 0;
                    while (treasuresCount < pointTreasureCount) {
                        CompletableFuture<String[]> cf = Action.digArea(client, point);
                        String[] treasures = cf.get();

                        treasuresCount -= treasures.length;

                        String strObj = Arrays.stream(treasures).collect(Collectors.joining());
                        Logger.log(ActionEnum.DIG, "Treasures: " + strObj);

                        //Меняем сокровища на золото
                        for (String treasure : treasures) {
                            //### CASH ###
                            int[] money = Action.cash(treasure);
                        }
                    }
                }




                //----------------------------------------

/*
                for (Point point : points) {
//                Logger.log(RequestEnum.ALL, "--- New Point ---");
//                Logger.log("x = " + point.getX() + "; y = " + point.getY());

                    //Пока есть сокровища и глубина позволяет - копать
                    int currentTreasureCount = pointTreasureCount;
                    while (currentTreasureCount > 0 && point.getDepth() < maxDepth) {
                        if (point.getDepth() >= 11) System.out.println("->>>>>>>>>>> ERROR - too much depth ");

                        //Проверка - если нет лицензий - то надо получить лицензии многопоточно
                        if (client.getLicenses() == null || client.getLicenses().size() == 0) {

                            //### LICENSE ###
                            List<License> licenses = Action.getLicenses(new int[]{});
                            client.setLicenses(licenses);
                        }

                        if (client.getLicenses() != null) {
                            List<License> updateLicenses = new ArrayList<>();
                            for (License license : client.getLicenses()) {
                                //Если число попыток копания этой лицензии не исчерпано
                                if (license.getDigUsed() < license.getDigAllowed()) {
                                    String[] treasures = dig(license, point, point.getDepth());

                                    //Если удалось копать
                                    if (treasures != null) {
                                        //Изменяем число сокровищ для координаты x,y
                                        currentTreasureCount -= treasures.length;
                                        //Обновляем глубину раскопок для точки
                                        point.setDepth(point.getDepth() + 1);

                                        //изменяем число попыток раскопок и текущую глубину
                                        license.setDigUsed(license.getDigUsed() + 1);

                                        //добавляем во временный массив обновленных лицензий
                                        if (license.getDigUsed() < license.getDigAllowed()) {
                                            updateLicenses.add(license);
                                        }

                                        //Меняем сокровища на золото
                                        for (String treasure : treasures) {
                                            //### CASH ###
                                            int[] money = Action.cash(treasure);
                                        }
                                    }
                                }
                            }
                            client.setLicenses(updateLicenses);
                        }
                    }
                }
*/
            }
        }

//        Logger.log("=================================");
//        Logger.log("Result : " + resMoney);
    }


    private String[] dig(License license, Point point, int depth) throws IOException, InterruptedException {
        Logger.log("Use License: " + license);
        //копаем - и находим список сокровищ на уровне
        //### DIG ###
        String[] treasures = Action.dig(license, point, depth);

        /*
        if (treasures != null) {
            //Меняем сокровища на золото
            for (String treasure : treasures) {
                //### CASH ###
                int[] money = Action.cash(treasure);
//                if (money != null && money.length > 0) {
//                    for (int moneyItem: money) {
//                        resMoney += moneyItem;
//                    }
//                }
            }
        }
        */

        return treasures;
    }

}