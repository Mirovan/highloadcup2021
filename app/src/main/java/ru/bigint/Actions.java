package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Actions {
    private static String address = "localhost";
    static {
        if (System.getenv("ADDRESS") != null) {
            address = System.getenv("ADDRESS");
        }
    }
    private final static String port = "8000";
    private final static String schema = "http";
    private final static String URI = schema + "://" + address + ":" + port;

    private final static int areaSize = 250;

    private final static int threadsCount = 4;

    public static String[] dig(Client client, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(client.getLicense().getId(), point.getX(), point.getY(), depth);
        //### DIG ###
        String[] treasures = RequestEndpoint.dig(URI, digRequest);

        return treasures;
    }


    /**
     * Возвращает карту сокровищ.
     * ключ - число сокровищ, значения - список координат в которых хранится суммарное число сокровищ
     * */
    public static Map<Integer, List<Point>> getTreasureMap() {
        long time = System.currentTimeMillis();

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        //Опрашиваем всю карту в заданных границах и получаем мапу
        for (int x = 1; x < areaSize; x++) {
            int y = 1;
            while (y < areaSize) {
                //Делаем threadsCount-число асинхронных запросов на запрос /explore
                List<ExploreRequest> requestList = new ArrayList<>();
                for (int k = 0; k < threadsCount && y < areaSize; k++) {
                    //### Explore ###
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
                        if (explore != null && explore.getAmount() != 0) {
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
//                        Logger.log(e.getMessage());
                    }
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + "(count=" + treasureMap.get(k).size() + "), ";
        }
        Logger.log("Treasures count: " + strTreasuresCount);
        Logger.log("Time for get treasure map: " + (System.currentTimeMillis() - time));

//        System.out.println("Treasures count: " + strTreasuresCount);
//        System.out.println("Time for get treasure map: " + (System.currentTimeMillis() - time));

        return treasureMap;
    }

    public static License getLicense(int[] arr) throws IOException, InterruptedException {
        return RequestEndpoint.postLicense(URI, arr);
    }

    public static int[] cash(String treasure) throws IOException, InterruptedException {
        return RequestEndpoint.cash(URI, treasure);
    }
}
