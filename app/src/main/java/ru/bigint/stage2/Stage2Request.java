package ru.bigint.stage2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.ActionEnum;
import ru.bigint.Constant;
import ru.bigint.LoggerUtil;
import ru.bigint.MapperUtils;
import ru.bigint.model.Client;
import ru.bigint.model.Point;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Stage2Request {

    private static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();


    public static Explore explore(ExploreRequest requestObj) {
        ActionEnum actionEnum = ActionEnum.EXPLORE;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestObj);
        } catch (JsonProcessingException e) {
            LoggerUtil.log(e.getMessage());
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(2))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        //Отправляем http-запрос
        CompletableFuture<Explore> cf = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    Explore responseObj = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, requestObj, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<Explore> resultMapper = new MapperUtils<>(Explore.class);
                            responseObj = resultMapper.convertToObject(httpResponse.body());
                        }
                    } else {
                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return responseObj;
                });


        Explore res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.log(actionEnum, e.getMessage());
        }

        return res;
    }


    public static License license(Integer[] requestObj) {
        ActionEnum actionEnum = ActionEnum.LICENSES;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestObj);
        } catch (JsonProcessingException e) {
            LoggerUtil.log(e.getMessage());
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(2))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        //Отправляем http-запрос
        CompletableFuture<License> cf = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    License responseObj = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, requestObj, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<License> resultMapper = new MapperUtils<>(License.class);
                            responseObj = resultMapper.convertToObject(httpResponse.body());
                        }
                    } else {
                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return responseObj;
                });


        License res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.log(actionEnum, e.getMessage());
        }

        return res;
    }


    public static String[] dig(DigRequest requestObj) {
        ActionEnum actionEnum = ActionEnum.DIG;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestObj);
        } catch (JsonProcessingException e) {
            LoggerUtil.log(e.getMessage());
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(2))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        //Отправляем http-запрос
        CompletableFuture<String[]> cf = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    String[] responseObj = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, requestObj, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<String[]> resultMapper = new MapperUtils<>(String[].class);
                            responseObj = resultMapper.convertToObject(httpResponse.body());
                        } else if (httpResponse.statusCode() == 404) {
                            responseObj = new String[]{};
                        }
                    } else {
                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return responseObj;
                });


        String[] res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.log(actionEnum, e.getMessage());
        }

        return res;
    }


    public static Integer[] cash(String requestObj) {
        ActionEnum actionEnum = ActionEnum.CASH;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestObj);
        } catch (JsonProcessingException e) {
            LoggerUtil.log(e.getMessage());
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(2))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        //Отправляем http-запрос
        CompletableFuture<Integer[]> cf = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    Integer[] responseObj = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, requestObj, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<Integer[]> resultMapper = new MapperUtils<>(Integer[].class);
                            responseObj = resultMapper.convertToObject(httpResponse.body());
                        } else if (httpResponse.statusCode() == 404) {
                            responseObj = new Integer[]{};
                        }
                    } else {
                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return responseObj;
                });


        Integer[] res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.log(actionEnum, e.getMessage());
        }

        return res;
    }


    /**
     * Возвращает список точек с сокровищами
     */
    public static List<Point> getPoints() {
        long time = System.currentTimeMillis();

        List<Point> res = new ArrayList<>();

        int treasureCount = 0;

        //Формирую список N-запросов для всей карты
        List<CompletableFuture<List<Point>>> cfList = new ArrayList<>();
        for (int x = 0; x < Constant.maxExploreX; x++) {
            int finalX = x;
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            cf.completeAsync(() -> {
                List<Point> list = AlgoUtils.binSearch(finalX, 1, Constant.maxExploreX);
                return list;
            });
            cfList.add(cf);

            //Каждые N-раз отправляем запросы на сервер
            if (x % Constant.threadsCountExplore == 0) {
                List<List<Point>> pointLists = cfList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                for (List<Point> list: pointLists) {
                    res.addAll(list);
//                    if (x > 140)
                        System.out.println("x=" + x);
                    Integer tresByX = list.stream().map(Point::getTreasuresCount).reduce(0, Integer::sum);
                    treasureCount = treasureCount + tresByX;
//                    if (x > 140)
                        System.out.println("tresByX: " + tresByX + "; allTres = " + treasureCount + "; Time: " + (System.currentTimeMillis() - time));
                }

                cfList.clear();
            }
        }


/*        int treasureCount = 0;
        int x = 0;
        for (int i = 0; i < cfList.size(); i = i + Constant.threadsCountExplore) {
            List<CompletableFuture<List<Point>>> cfPartList =
                    cfList.subList(
                            i,
                            Math.min(i+Constant.threadsCountExplore, cfList.size())
                    );

            //Ждем все потоки
//            CompletableFuture.allOf(cfPartList.toArray(new CompletableFuture[0])).join();

            List<List<Point>> pointLists = cfPartList.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

//            res.addAll(res);
            for (List<Point> list: pointLists) {
                res.addAll(list);
                if (x > 140) System.out.println("x=" + x);
                Integer tresByX = list.stream().map(Point::getTreasuresCount).reduce(0, Integer::sum);
                treasureCount = treasureCount + tresByX;
                if (x > 140) System.out.println("tresByX: " + tresByX + "; allTres = " + treasureCount + "; Time: " + (System.currentTimeMillis() - time));
                x++;
            }*/

//
//            for (CompletableFuture<List<Point>> cfItem: cfPartList) {
//                try {
//                    list = cfItem.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    LoggerUtil.log(ActionEnum.EXPLORE, e.getMessage());
//                }
//                if (list != null) {
//                    res.addAll(list);
//
//                    if (x > 135) {
//                        System.out.println("x=" + x);
//                        Integer tresByX = list.stream().map(Point::getTreasuresCount).reduce(0, Integer::sum);
//                        treasureCount = treasureCount + tresByX;
//                        System.out.println("tresByX: " + tresByX + "; allTres = " + treasureCount);
//                    }
//                }
//                x++;
//            }
//        }

        return res;
    }

}
