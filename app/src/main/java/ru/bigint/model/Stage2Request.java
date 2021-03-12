package ru.bigint.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.ActionEnum;
import ru.bigint.Constant;
import ru.bigint.LoggerUtil;
import ru.bigint.MapperUtils;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;
import ru.bigint.stage2.AlgoUtils;
import ru.bigint.stage2.Req;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Stage2Request {

    private static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    private static ExecutorService threadPoolExplore = Executors.newFixedThreadPool(Constant.threadsCountExplore);
    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);


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
        //Формирую список N-запросов для всей карты
        List<CompletableFuture<List<Point>>> cfList = new ArrayList<>();
        for (int x = 0; x < Constant.maxExploreX; x++) {
            int finalX = x;
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            cf.completeAsync(() -> AlgoUtils.binSearch(finalX, 1, Constant.mapSize), threadPoolExplore);
            cfList.add(cf);
        }

        List<Point> res = cfList.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return res;
    }


    /**
     * Асинхронные раскопки
     */
    public static List<DigWrapper> dig(List<License> licenses, Stack<Point> digPointStack) {
        //Формируем список с объектами-запросами
        List<DigRequest> requestList = new ArrayList<>();
        for (License license : licenses) {
            for (int i = license.getDigUsed(); i < license.getDigAllowed(); i++) {
                if (!digPointStack.isEmpty()) {
                    Point point = digPointStack.pop();
                    DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth()+1);
                    requestList.add(digRequest);
                }
            }
        }

        //список с асинхронными запросами
        List<CompletableFuture<DigWrapper>> listCf = new ArrayList<>();
        for (int i = 0; i < requestList.size(); i++) {
            DigRequest requestObj = requestList.get(i);

            CompletableFuture<DigWrapper> cf = new CompletableFuture<>();

            cf.completeAsync(() -> Req.dig(requestObj, licenses), threadPoolDig);
            listCf.add(cf);
        }

        List<DigWrapper> res = listCf.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return res;
    }
}
