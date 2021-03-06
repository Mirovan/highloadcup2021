package ru.bigint;

import ru.bigint.model.*;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ActionMultiRequest<T, U> {

    private Class<T> requestClassType;
    private Class<U> responseClassType;

    public ActionMultiRequest(Class<T> requestClassType, Class<U> responseClassType) {
        this.requestClassType = requestClassType;
        this.responseClassType = responseClassType;
    }


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
                    pointList.add(new Point(explore.getArea().getPosX(), explore.getArea().getPosY(), 1, explore.getAmount()));
                    treasureMap.put(treasureCount, pointList);
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + "(count=" + treasureMap.get(k).size() + "), ";
        }
        LoggerUtil.log("Treasures count: " + strTreasuresCount);
        LoggerUtil.log("Time for get treasure map: " + (System.currentTimeMillis() - time));

        return treasureMap;
    }


    /**
     * Возвращает карту сокровищ.
     * ключ - число сокровищ, значения - список координат в которых хранится суммарное число сокровищ
     */
    public List<U> getTreasureMap(int startX, int startY) {
        ActionEnum actionEnum = ActionEnum.EXPLORE;

        //Делаем threadsCount-число асинхронных запросов на запрос /explore
        List<T> requestList = new ArrayList<>();
        for (int i = 0; i < Constant.threadsCount; i++) {
            //### Explore ###
            ExploreRequest exploreRequest = new ExploreRequest(startX, startY + i, 1, 1);
            requestList.add((T) exploreRequest);
        }

        List<U> exploreList = asyncResponseResult(requestList, actionEnum);
        return exploreList;
    }


    /**
     * Возвращает список лицензий
     */
    public List<U> getLicenses(List<T> list) {
        ActionEnum actionEnum = ActionEnum.LICENSES;

        //Делаем threadsCount-число асинхронных запросов на запрос /explore
//        List<T> requestList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            requestList.add(list.get(i));
//        }

        List<U> licenses = asyncResponseResult(list, actionEnum);

        return licenses;
    }


    /**
     * Возвращает результат раскопок
     */
    public List<U> dig(Client client, List<Point> points, List<License> licenses) {
        ActionEnum actionEnum = ActionEnum.DIG;

        //Делаем асинхронные запросы по числу лицензий
        List<T> requestList = new ArrayList<>();
        for (int i = 0; i < licenses.size() && i < points.size(); i++) {
            Point point = points.get(i);
            License license = licenses.get(i);
            DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth());
            requestList.add((T) digRequest);
        }

        List<U> treasureList = asyncResponseResult(requestList, actionEnum);
        return treasureList;
    }


    /**
     * Возвращает список с результатом асинхронных запросов
     * */
    private List<U> asyncResponseResult(List<T> requestList, ActionEnum actionEnum) {
        MapperUtils<U> mapper = new MapperUtils<>(responseClassType);

        List<U> resultList = new ArrayList<>();

        //Получаем результаты асинхронных запросов
        ClientRequest<T> clientRequest = new ClientRequest();

        List<CompletableFuture<HttpResponse<String>>> cfReponseList = clientRequest.concurrentPost(actionEnum, requestList);
        for (int i = 0; i < cfReponseList.size(); i++) {
            CompletableFuture<HttpResponse<String>> cf = cfReponseList.get(i);
            try {
                U resultObject = null;
                HttpResponse<String> response = cf.get();
                LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
                if (response != null) {
                    resultObject = mapper.convertToObject(response.body());
                } else {
//                    Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null: " + response);
                }

                //Если объект не пустой
                if (resultObject != null) {
                    resultList.add(resultObject);
                }
            } catch (ExecutionException | InterruptedException e) {
//                        Logger.log(e.getMessage());
            }
        }

        return resultList;
    }

}