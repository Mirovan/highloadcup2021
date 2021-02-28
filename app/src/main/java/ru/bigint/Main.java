package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.*;

public class Main {

    private final static int maxDepth = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
//        Logger.log("--- Running App ---");

        long startTime = System.currentTimeMillis();

        Main main = new Main();
        main.runGame();

//        Logger.log("Time: " + (System.currentTimeMillis() - startTime));
    }

    private void runGame() throws IOException, InterruptedException {
//        Logger.log("--- Play Game ---");
//        Logger.log("OS: " + System.getProperty("os.name"));
//        Logger.log("URI: " + URI);

        int resMoney = 0;

//        RequestEndpoint.healthCheck(URI);

        Client client = new Client();

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = Action.getExplore();

        List<Integer> treasureAmountList = new ArrayList<>(treasureMap.keySet());
        Collections.sort(treasureAmountList, Comparator.reverseOrder());

        String strTres = "";
        for (Integer k : treasureMap.keySet()) {
            strTres += k + "=>" + treasureMap.get(k).size() + "; ";
        }
        Logger.log(strTres);

        //Копаем сначала в точках с максимальным содержанием сокровищ
        for (Integer pointTreasureCount : treasureAmountList) {
            //ToDo: for test
//            if (pointTreasureCount < 2) break;

            List<Point> points = treasureMap.get(pointTreasureCount);
            if (points != null) {

                for (Point point : points) {
//                Logger.log(RequestEnum.ALL, "--- New Point ---");
//                Logger.log("x = " + point.getX() + "; y = " + point.getY());
                    Logger.log("=== Treasure count: " + pointTreasureCount + "; x = " + point.getX() + "; y = " + point.getY() + " ===");


                    //Пока есть сокровища и глубина позволяет - копать
                    int foundTreasureCount = 0;
                    while (foundTreasureCount < pointTreasureCount && point.getDepth() < maxDepth) {
                        Logger.log("foundTreasureCount = " + foundTreasureCount);
                        if (point.getDepth() >= 11) System.out.println("->>>>>>>>>>> ERROR - too much depth ");

                        String[] treasures = dig(client, point);

                        //Если удалось копать
                        if (treasures != null) {
                            //Изменяем текущее число сокровищ для координаты x,y
                            foundTreasureCount += treasures.length;

                            //Меняем сокровища на золото
                            for (String treasure : treasures) {
                                if (client.getMoney() == null) client.setMoney(new LinkedList<>());
                                //### CASH ###
                                Integer[] money = Action.cash(treasure);
                                if (money == null) money = new Integer[0];
                                client.getMoney().addAll(Arrays.asList(money));
                            }
                        }

                    }
                }
            }
        }

//        Logger.log("=================================");
//        Logger.log("Result : " + resMoney);
    }


    private String[] dig(Client client, Point point) throws IOException, InterruptedException {
        //Проверка - если нет лицензий - то надо получить лицензии многопоточно
        if (client.getLicenses() == null || client.getLicenses().size() == 0) {
            //### LICENSE ###
            List<License> licenses = Action.getLicenses(client);
            client.setLicenses(licenses);
        }

        //Выбираем лицензию
        License license = null;
        for (License item : client.getLicenses()) {
            //Если число попыток копания этой лицензии не исчерпано
            if (item.getDigUsed() < item.getDigAllowed()) {
                license = item;
                break;
            }
        }

        String[] treasures = null;

        if (license != null) {
            Logger.log("Use License: " + license);
            //копаем - и находим список сокровищ на уровне
            //### DIG ###
            treasures = Action.dig(license, point, point.getDepth());

            //Копнуть удалось, т.е. если сокровища найдены или мы знаем что их нет - удаляем истекшие лицензии
            if (treasures != null) {
                //Обновляем глубину раскопок для точки
                point.setDepth(point.getDepth() + 1);

                //изменяем число попыток раскопок и текущую глубину
                license.setDigUsed(license.getDigUsed() + 1);

                List<License> updateLicenses = new ArrayList<>();
                for (License item : client.getLicenses()) {
                    //Если число попыток копания этой лицензии не исчерпано
                    if (license.getDigUsed() < license.getDigAllowed()) {
                        updateLicenses.add(item);
                    }
                }
                client.setLicenses(updateLicenses);
            }
        }

        return treasures;
    }

}