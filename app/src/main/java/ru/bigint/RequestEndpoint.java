package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RequestEndpoint {
    private final static int areaSize = 250;

    private final static int threadsCount = 4;

    private static int retryCount = 5;

    public static Explore explore(ExploreRequest exploreRequest) throws IOException, InterruptedException {
//        Logger.log("-- Explore --");
//        String body = Request.doPost(RequestEnum.EXPLORE, exploreRequest);
//        MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);
//        Explore explore = mapper.convertToObject(body);
//        return explore;
        return null;
    }


    public static License postLicense(int[] licenseRequest) throws IOException, InterruptedException {
//        Logger.log("-- Licence post --");
        RequestAction requestAction = RequestAction.LICENSES;
        Logger.log(requestAction, ">>> Request to: " + requestAction + "; Object = " + licenseRequest);


        HttpResponse<String> response = null;
        int retry = 1;
        do {
            response = Request.doPost(requestAction, licenseRequest);
            if (response != null) {
                Logger.log(requestAction, "<<< Response: " + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(requestAction, "<<< Response: " + requestAction + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response.statusCode() == 200) {
                break;
            }
        } while (retry < retryCount);

        License license = null;
        if (response.statusCode() == 200) {
            MapperUtils<License> mapper = new MapperUtils<>(License.class);
            license = mapper.convertToObject(response.body());
        }

        return license;
    }


    public static License license() throws IOException, InterruptedException {
//        Logger.log("-- Licence post --");
//        String body = Request.doGet(RequestEnum.LICENSES);
//        MapperUtils<License> mapper = new MapperUtils<>(License.class);
//        License license = mapper.convertToObject(body);
//        return license;
        return null;
    }


    public static String[] dig(DigRequest digRequest) throws IOException, InterruptedException {
//        Logger.log("-- Dig --");
        RequestAction requestAction = RequestAction.DIG;
        Logger.log(requestAction, ">>> Request to: " + requestAction + "; Object = " + digRequest);

        HttpResponse<String> response;
        int retry = 1;
        do {
            response = Request.doPost(requestAction, digRequest);
            if (response != null) {
                Logger.log(requestAction, "<<< Response: " + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(requestAction, "<<< Response: " + requestAction + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response.statusCode() == 200 || response.statusCode() == 404) {
                break;
            }
        } while (retry < retryCount);

        String[] dig;
        if (response.statusCode() == 200) {
            MapperUtils<String[]> mapper = new MapperUtils<>(String[].class);
            dig = mapper.convertToObject(response.body());
        } else {
            dig = new String[0];
        }
        return dig;
    }


    public static int[] cash(String treasure) throws IOException, InterruptedException {
//        Logger.log("-- Cash --");
        RequestAction requestAction = RequestAction.CASH;
        Logger.log(requestAction, ">>> Request to: " + requestAction + "; Object = " + treasure);

        HttpResponse<String> response;
        int retry = 1;
        do {
            response = Request.doPost(requestAction, treasure);
            if (response != null) {
                Logger.log(requestAction, "<<< Response: " + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(requestAction, "<<< Response: " + requestAction + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response.statusCode() == 200) {
                break;
            }
        } while (retry < retryCount);

        int[] money = null;
        if (response.statusCode() == 200) {
            MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
            money = mapper.convertToObject(response.body());
        }

        return money;
    }


    public static String healthCheck() throws IOException, InterruptedException {
        String body = Request.doGet(RequestAction.HEALTH_CHECK);
//        Logger.log(body);
        return body;
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
                List<CompletableFuture<String>> cfReponseList = Request.concurrentCalls(RequestAction.EXPLORE, requestList);
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
//        Logger.log("Treasures count: " + strTreasuresCount);
//        Logger.log("Time for get treasure map: " + (System.currentTimeMillis() - time));

//        System.out.println("Treasures count: " + strTreasuresCount);
//        System.out.println("Time for get treasure map: " + (System.currentTimeMillis() - time));

        return treasureMap;
    }
}