package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ActionMultiRequest {

    /**
     * Возвращает карту сокровищ в один поток
     */
    public static Map<Integer, List<Point>> getTreasureMapOneThread() throws IOException, InterruptedException {
        long time = System.currentTimeMillis();

        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        for (int x = 1; x < Constant.areaSize; x++) {
            for (int y = 1; y < Constant.areaSize; y++) {
                ExploreRequest exploreRequest = new ExploreRequest(x, y, 1, 1);
                Explore explore = Action.explore(exploreRequest);

                //Если сокровища в точке есть
                if (explore != null && explore.getAmount() != 0) {
                    int treasureCount = explore.getAmount();

                    //обновляем список с координатами сокровищ
                    List<Point> pointList = null;
                    if (treasureMap.containsKey(treasureCount)) {
                        pointList = treasureMap.get(treasureCount);
                    } else {
                        pointList = new ArrayList<>();
                    }
                    pointList.add(new Point(explore.getArea().getPosX(), explore.getArea().getPosY()));
                    treasureMap.put(treasureCount, pointList);
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + "(count=" + treasureMap.get(k).size() + "), ";
        }
        Logger.log("Treasures count: " + strTreasuresCount);
        Logger.log("Time for get treasure map: " + (System.currentTimeMillis() - time));

        return treasureMap;
    }


    /**
     * Возвращает карту сокровищ.
     * ключ - число сокровищ, значения - список координат в которых хранится суммарное число сокровищ
     */
    public static Map<Integer, List<Point>> getTreasureMap() {
        long time = System.currentTimeMillis();

        MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        //Опрашиваем всю карту в заданных границах и получаем мапу
        for (int x = 1; x < Constant.areaSize; x++) {
            int y = 1;
            while (y < Constant.areaSize) {
                //Делаем threadsCount-число асинхронных запросов на запрос /explore
                List<ExploreRequest> requestList = new ArrayList<>();
                for (int k = 0; k < Constant.threadsCount && y < Constant.areaSize; k++) {
                    //### Explore ###
                    ExploreRequest exploreRequest = new ExploreRequest(x, y, 1, 1);
                    requestList.add(exploreRequest);
                    y++;
                }

                //Получаем результаты асинхронных запросов
                ClientRequest<ExploreRequest> clientRequest = new ClientRequest();
                List<CompletableFuture<String>> cfReponseList = clientRequest.concurrentPost(ActionEnum.EXPLORE, requestList);
                for (int i = 0; i < cfReponseList.size(); i++) {
                    CompletableFuture<String> response = cfReponseList.get(i);
                    String responseBody = null;
                    try {
//                        Logger.log("--- Next point ---");
//                        Logger.log("x = " + x + "; y = " + (tempY+i));

                        responseBody = response.get();
//                        Logger.log("Response: " + responseBody);

                        Explore explore = mapper.convertToObject(responseBody);

                        //Если сокровища в точке есть
                        if (explore != null && explore.getAmount() != 0) {
                            int treasureCount = explore.getAmount();

                            //обновляем список с координатами сокровищ
                            List<Point> pointList = null;
                            if (treasureMap.containsKey(treasureCount)) {
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


    /**
     * Возвращает список лицензий
     */
    public static List<License> getLicenses(int[] arr) {

        //Делаем threadsCount-число асинхронных запросов на запрос /explore
        List<int[]> requestList = new ArrayList<>();
        for (int i = 0; i < Constant.threadsCount; i++) {
            requestList.add(arr);
        }

        MapperUtils<License> mapper = new MapperUtils<>(License.class);

        List<License> licenses = new ArrayList<>();

        //Получаем результаты асинхронных запросов
        ClientRequest<int[]> clientRequest = new ClientRequest();
        List<CompletableFuture<String>> cfReponseList = clientRequest.concurrentPost(ActionEnum.LICENSES, requestList);
        for (int i = 0; i < cfReponseList.size(); i++) {
            CompletableFuture<String> response = cfReponseList.get(i);
            String responseBody;
            try {
                responseBody = response.get();
//                        Logger.log("Response: " + responseBody);

                License license = mapper.convertToObject(responseBody);

                //Если сокровища в точке есть
                if (license != null) {
                    licenses.add(license);
                }
            } catch (ExecutionException | InterruptedException e) {
//                        Logger.log(e.getMessage());
            }
        }

        return licenses;
    }

}