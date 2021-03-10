package ru.bigint.stage2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.ActionEnum;
import ru.bigint.Constant;
import ru.bigint.LoggerUtil;
import ru.bigint.MapperUtils;
import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
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
        List<Point> res = new ArrayList<>();

        int treasureCount = 0;

        //Формирую список N-запросов для всей карты
        List<CompletableFuture<List<Point>>> cfList = new ArrayList<>();
        for (int x = 0; x < Constant.maxExploreX; x++) {
            int finalX = x;
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            cf.completeAsync(() -> {
                List<Point> list = AlgoUtils.binSearch(finalX, 1, Constant.mapSize);
                return list;
            });
            cfList.add(cf);

            //Каждые N-раз отправляем запросы на сервер
            if (x % Constant.threadsCountExplore == 0) {
                List<List<Point>> pointLists = cfList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                for (List<Point> list : pointLists) {
                    res.addAll(list);
                    Integer tresByX = list.stream().map(Point::getTreasuresCount).reduce(0, Integer::sum);
                    treasureCount = treasureCount + tresByX;
                }

                cfList.clear();
            }
        }

        return res;
    }


    /**
     * Асинхронные раскопки
     */
    public static List<DigWrapper> dig(List<License> licenses, Stack<Point> digPointStack) {
        ActionEnum actionEnum = ActionEnum.DIG;
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        List<DigWrapper> res = new ArrayList<>();

        //Формируем список с запросами
        List<DigRequest> requestList = new ArrayList<>();
        for (License license : licenses) {
            for (int i = license.getDigUsed(); i < license.getDigAllowed(); i++) {
                if (!digPointStack.isEmpty()) {
                    Point point = digPointStack.pop();
                    DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth());
                    requestList.add(digRequest);
                }
            }
        }

        List<CompletableFuture<DigWrapper>> listCf = null;
        for (int i = 0; i < requestList.size(); i++) {
            DigRequest requestObj = requestList.get(i);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = null;
            try {
                requestBody = objectMapper.writeValueAsString(requestObj);
            } catch (JsonProcessingException e) {
//                Logger.log(e.getMessage());
            }

            HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Content-Type", "application/json; charset=UTF-8")
                            .timeout(Duration.ofSeconds(5))
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

            //Отправляем http-запрос
            CompletableFuture<DigWrapper> cf = httpClient
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        String[] treasures = null;
                        if (httpResponse != null) {
                            LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response code: " + httpResponse.statusCode() + "; Response body: " + httpResponse.body());
                            if (httpResponse.statusCode() == 200) {
                                MapperUtils<String[]> resultMapper = new MapperUtils<>(String[].class);
                                treasures = resultMapper.convertToObject(httpResponse.body());
                            } else if (httpResponse.statusCode() == 404 || httpResponse.statusCode() == 403) {
                                treasures = new String[]{};
                            }
                        } else {
                            LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                        }

                        return new DigWrapper(requestObj, treasures);
                    });

            listCf.add(cf);

            if (i % Constant.threadsCountDig == 0) {
                List<DigWrapper> digListRes = listCf.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                res.addAll(digListRes);

                listCf.clear();
            }
        }

        return res;
    }
}
