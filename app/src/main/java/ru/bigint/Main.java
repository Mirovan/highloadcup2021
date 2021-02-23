package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        long time = System.currentTimeMillis();
//        Logger.log("--- Play Game ---");
//        Logger.log("OS: " + System.getProperty("os.name"));
//        Logger.log("URI: " + URI);

        int resMoney = 0;

//        RequestEndpoint.healthCheck(URI);

        Client client = new Client();

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = RequestEndpoint.getTreasureMap();

        List<Integer> treasureAmountList = new ArrayList<>(treasureMap.keySet());
        for (int pointTreasureCount = treasureAmountList.size()-1; pointTreasureCount >= 0; pointTreasureCount--) {
            List<Point> points = treasureMap.get(pointTreasureCount);
            for (Point point: points) {
//                Logger.log(RequestEnum.ALL, "--- New Point ---");
                Logger.log("x = " + point.getX() + "; y = " + point.getY());

                //Пока есть сокровища и глубина позволяет - копать
                int depth = 1;
                int currentTreasureCount = pointTreasureCount;
                while (currentTreasureCount > 0 && depth <= maxDepth) {
                    //Проверка - если нет лицензии на раскопки или нельзя копать - то надо получить лицензию
                    if (client.getLicense() == null
                            || client.getLicense().getDigUsed() >= client.getLicense().getDigAllowed()) {
                        //### LICENSE ###
                        License license = Actions.getLicense(new int[]{});
                        client.setLicense(license);
                    }

                    //Если можно копать
                    if (client.getLicense() != null && client.getLicense().getDigUsed() < client.getLicense().getDigAllowed()) {
                        //копаем - и находим список сокровищ на уровне
                        //### DIG ###
                        String[] treasures = Actions.dig(client, point, depth);

                        if (treasures != null) {
                            //изменяем число попыток раскопок и текущую глубину
                            client.getLicense().setDigUsed(client.getLicense().getDigUsed() + 1);
                            depth++;

                            //Изменяем число сокровищ для координаты x,y
                            currentTreasureCount -= treasures.length;

                            //Меняем сокровища на золото
                            for (String treasure : treasures) {
                                //### CASH ###
                                int[] money = Actions.cash(treasure);
                                if (money != null && money.length > 0) {
                                    resMoney += money[0];
                                }
                            }
                        }
                    }
                }
            }
        }

//        Logger.log("=================================");
//        Logger.log("Result : " + resMoney);
    }

}