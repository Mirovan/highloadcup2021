package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Action {

    public static License license(int[] arr) throws IOException, InterruptedException {
        return ActionRequest.license(arr);
    }


    public static String[] dig(License license, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), depth);
        String[] treasures = ActionRequest.dig(digRequest);
        return treasures;
    }


    public static int[] cash(String treasure) throws IOException, InterruptedException {
        return ActionRequest.cash(treasure);
    }


    public static Balance balance() throws IOException, InterruptedException {
        return ActionRequest.balance();
    }


    public static Explore explore(ExploreRequest exploreRequest) throws IOException, InterruptedException {
        return ActionRequest.explore(exploreRequest);
    }


    public static Map<Integer, List<Point>> getExplore() {
        long time = System.currentTimeMillis();

        ActionMultiRequest<ExploreRequest, Explore> actionMultiRequest = new ActionMultiRequest<>(ExploreRequest.class, Explore.class);

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        //Опрашиваем всю карту в заданных границах и получаем мапу
        for (int x = 1; x < Constant.areaSize; x++) {
            for (int y = 1; y < Constant.areaSize; y = y + Constant.threadsCount) {
                List<Explore> treasures = actionMultiRequest.getTreasureMap(x, y);

                for (Explore treasure : treasures) {
                    //Если сокровища в точке есть
                    if (treasure != null && treasure.getAmount() != 0) {
                        int treasureCount = treasure.getAmount();

                        //обновляем список с координатами сокровищ
                        List<Point> pointList = null;
                        if (treasureMap.containsKey(treasureCount)) {
                            pointList = treasureMap.get(treasureCount);
                        } else {
                            pointList = new ArrayList<>();
                        }
                        pointList.add(new Point(treasure.getArea().getPosX(), treasure.getArea().getPosY(), 1));
                        treasureMap.put(treasureCount, pointList);
                    }
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + "(count=" + treasureMap.get(k).size() + "), ";
        }
        Logger.log(ActionEnum.EXPLORE, "Treasures count: " + strTreasuresCount);
        Logger.log(ActionEnum.EXPLORE, "Time for get treasure map: " + (System.currentTimeMillis() - time));

        return treasureMap;
    }


    public static List<License> getLicenses(int[] arr) {
        ActionMultiRequest<int[], License> actionMultiRequest = new ActionMultiRequest<>(int[].class, License.class);
        return actionMultiRequest.getLicenses(arr);
    }


    public static CompletableFuture<String[]> digArea(Client client, Point point) throws ExecutionException, InterruptedException {
        MapperUtils<String[]> digMapperUtils = new MapperUtils<>(String[].class);

        CompletableFuture<String[]> res = Async.getLicense(client, new int[]{})
                .thenCompose(license -> {
                    List<License> licenses = new ArrayList<>();
                    licenses.add(license);
                    client.setLicenses(licenses);
                    DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth());
                    return Async.dig(digRequest);
                })
                .thenApply(HttpResponse::body)
                .thenApply(digMapperUtils::convertToObject);

        return res;
    }

}


//    CompletableFuture result =
//      getMovieList(showDetails.getShowTime().getDay())
//            .thenCompose(movies -> selectMovie(movies))
//            .thenCompose(movie ->
//                  selectSeats(showDetails.getShowTime())
//                      .thenApply(showDetails1 -> applyPromoCode(showDetails1,promoCode))
//                      .thenCompose(showDetails2 -> getTicketPrice(showDetails2))
//             );