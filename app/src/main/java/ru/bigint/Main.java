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
    private static String address = "localhost";
    static {
        if (System.getenv("ADDRESS") != null) {
            address = System.getenv("ADDRESS");
        }
    }
    private final static String port = "8000";
    private final static String schema = "http";
    private final static String URI = schema + "://" + address + ":" + port;

    private final static int areaSize = 500;
    private final static int maxDepth = 10;

    private final static int threadsCount = 3;


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
        Map<Integer, List<Point>> treasureMap = getTreasureMap();

        List<Integer> treasureAmountList = new ArrayList<>(treasureMap.keySet());
        for (int pointTreasureCount = treasureAmountList.size()-1; pointTreasureCount >= 0; pointTreasureCount--) {
            List<Point> points = treasureMap.get(pointTreasureCount);
            for (Point point: points) {
                Logger.log("--- New Point ---");
                Logger.log("x = " + point.getX() + "; y = " + point.getY());

                //Пока есть сокровища и глубина позволяет - копать
                int depth = 1;
                int currentTreasureCount = pointTreasureCount;
                while (currentTreasureCount > 0 && depth <= maxDepth) {
                    //Проверка - если нет лицензии на раскопки или нельзя копать - то надо получить лицензию
                    if (client.getLicense() == null
                            || client.getLicense().getDigUsed() >= client.getLicense().getDigAllowed()) {
                        //### LICENSE ###
                        License license = RequestEndpoint.postLicense(URI, new int[]{});
                        client.setLicense(license);
                    }

                    //Если можно копать
                    if (client.getLicense() != null && client.getLicense().getDigUsed() < client.getLicense().getDigAllowed()) {
                        //копаем - и находим список сокровищ на уровне
                        DigRequest digRequest = new DigRequest(client.getLicense().getId(), point.getX(), point.getY(), depth);
                        //### DIG ###
                        String[] treasures = RequestEndpoint.dig(URI, digRequest);

                        //изменяем число попыток раскопок и текущую глубину
                        client.getLicense().setDigUsed(client.getLicense().getDigUsed() + 1);
                        depth++;

                        if (treasures != null) {
                            //Изменяем число сокровищ для координаты x,y
                            currentTreasureCount -= treasures.length;

                            //Меняем сокровища на золото
                            for (String treasure : treasures) {
                                //### CASH ###
                                int[] money = RequestEndpoint.cash(URI, treasure);
                                if (money != null && money.length > 0) {
                                    resMoney += money[0];
                                }
                            }
                        }
                    }
                }
            }
        }

        Logger.log("=================================");
        Logger.log("Result : " + resMoney);
    }


    /**
     * Возвращает карту сокровищ.
     * ключ - число сокровищ, значения - список координат в которых хранится суммарное число сокровищ
     * */
    private Map<Integer, List<Point>> getTreasureMap() {
        long time = System.currentTimeMillis();

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        //Опрашиваем всю карту и получаем сет из клеток с максимальным числом сокровищ
        for (int x = 1; x < areaSize; x++) {
            int y = 1;
            while (y < areaSize) {
                //Делаем threadsCount-число асинхронных запросов на запрос /explore
                List<ExploreRequest> requestList = new ArrayList<>();
                for (int k = 0; k < threadsCount && y < areaSize; k++) {
                    ExploreRequest exploreRequest = new ExploreRequest(x, y, 1, 1);
                    requestList.add(exploreRequest);
                    y++;
                }


                //Получаем результаты асинхронных запросов
                List<CompletableFuture<String>> cfReponseList = Request.concurrentCalls(URI+"/explore", requestList);
                for (int i = 0; i < cfReponseList.size(); i++) {
                    CompletableFuture<String> response = cfReponseList.get(i);
                    String responseBody = null;
                    try {
//                        Logger.log("--- Next point ---");
//                        Logger.log("x = " + x + "; y = " + (tempY+i));

                        responseBody = response.get();
//                        Logger.log("Response: " + responseBody);

                        MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);
                        Explore explore = mapper.convertToObject(responseBody);

                        //Если сокровища в точке есть
                        if (explore != null) {
                            int treasureCount = explore.getAmount();

                            //обновляем список с координатами сокровищ
                            List<Point> pointList = null;
                            if ( treasureMap.containsKey(treasureCount) ) {
                                pointList = treasureMap.get(treasureCount);
                            } else {
                                pointList = new ArrayList<>();
                            }
                            pointList.add(new Point(explore.getArea().getPosX(), explore.getArea().getPosY()));
                            treasureMap.put(treasureCount, pointList);
                        }

                    } catch (ExecutionException | InterruptedException e) {
                        Logger.log(e.getMessage());
                    }
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + ", ";
        }
        Logger.log("Treasures count: " + strTreasuresCount);
        Logger.log("Time for get treasure map: " + (System.currentTimeMillis() - time));

        System.out.println("Treasures count: " + strTreasuresCount);
        System.out.println("Time for get treasure map: " + (System.currentTimeMillis() - time));


        return treasureMap;
    }

}