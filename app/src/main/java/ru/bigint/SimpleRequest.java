package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.*;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Balance;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SimpleRequest {

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
                        } else {
                            LoggerUtil.log("Explore Error: " + httpResponse.body());
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
            e.printStackTrace();

//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
        }

        return res;
    }


    public static License license(Integer[] requestObj, Client client) {
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
                        } else if (httpResponse.statusCode() == 409) {
                            //no more active licenses allowed
//                            String stOut = client.getLicenses().stream().map(item -> item.toString()).collect(Collectors.joining());
//                            LoggerUtil.log("License Error: " + httpResponse.body() + "; client.licenses=" + client.getLicenses().size() + "->>" + stOut);
                        } else if (httpResponse.statusCode() == 502) {
                            //RPC failed
//                            LoggerUtil.log("License Error: " + httpResponse.body());
                        } else {
//                            LoggerUtil.log("License Error: " + httpResponse.body());
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
            e.printStackTrace();
//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
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
                        } else {
//                            LoggerUtil.log("Dig Error: " + httpResponse.body());
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
            e.printStackTrace();

//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
        }

        return res;
    }


    public static DigWrapper dig(DigRequestWrapper digRequestWrapper) {
        ActionEnum actionEnum = ActionEnum.DIG;
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        //Формирую запрос
        Point point = digRequestWrapper.getDigPoint();
        License license = digRequestWrapper.getLicense();
        DigRequest requestObj = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth() + 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(requestObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

//                Logger.log(e.getMessage());
        }

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(5))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        DigWrapper res = null;
        try {
            res = httpClient
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        String[] treasures = null;
                        if (httpResponse != null) {
//                            if (licenses.stream()
//                                    .anyMatch(item -> item.getId() == requestObj.getLicenseID())) {
//
//                                License lic = licenses.stream()
//                                        .filter(item -> item.getId() == requestObj.getLicenseID())
//                                        .findFirst()
//                                        .get();
//                                LoggerUtil.logRequestResponse(actionEnum, " lic=" + lic.toString() + "; requestObj=" + requestObj.toString(), httpResponse);
//                            }

                            LoggerUtil.logRequestResponse(actionEnum, "License=" + digRequestWrapper.getLicense() + "; requestObj=" + requestObj.toString(), httpResponse);

                            if (httpResponse.statusCode() == 200) {
                                MapperUtils<String[]> resultMapper = new MapperUtils<>(String[].class);
                                treasures = resultMapper.convertToObject(httpResponse.body());
                            } else if (httpResponse.statusCode() == 404 || httpResponse.statusCode() == 403) {
                                treasures = new String[]{};
                            } else if (httpResponse.statusCode() == 422) {
//                                LoggerUtil.log("Dig Error: " + lic + "; " + requestObj.getLicenseID() + "; " + requestObj + "; Response Code: " + httpResponse.statusCode() + "; Body: " + httpResponse.body());
                                treasures = new String[]{};
                            }
                        } else {
                            LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                        }

                        return new DigWrapper(point, license, treasures);
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
        }

        return res;
    }


    public static CashWrapper cash(String requestObj) {
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
        CompletableFuture<CashWrapper> cf = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(httpResponse -> {
                    Integer[] responseObj = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, requestObj, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<Integer[]> resultMapper = new MapperUtils<>(Integer[].class);
                            responseObj = resultMapper.convertToObject(httpResponse.body());
                        } else if (httpResponse.statusCode() == 503) {
                            //service unavailable
//                            LoggerUtil.log("Cash Error: " + httpResponse.body());
                        } else {
//                            LoggerUtil.log("Cash Error: " + httpResponse.body());
                        }
                    } else {
//                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return new CashWrapper(requestObj, responseObj);
                });


        CashWrapper res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
        }

        return res;
    }


//    public static void cashNoResponse(String requestObj) {
//        ActionEnum actionEnum = ActionEnum.CASH;
//
//        String url = Constant.SERVER_URI + actionEnum.getRequest();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = null;
//        try {
//            requestBody = objectMapper.writeValueAsString(requestObj);
//        } catch (JsonProcessingException e) {
//            LoggerUtil.log(e.getMessage());
//        }
//
//        HttpRequest httpRequest =
//                HttpRequest.newBuilder()
//                        .uri(URI.create(url))
//                        .header("Content-Type", "application/json; charset=UTF-8")
//                        .timeout(Duration.ofSeconds(2))
//                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                        .build();
//
//        //Отправляем http-запрос
//        CompletableFuture<HttpResponse<String>> cf = httpClient.
//                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//
//        CashWrapper res = null;
//        try {
//            res = cf.get();
//        } catch (InterruptedException | ExecutionException e) {
//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
//        }
//
//        return res;
//    }


    /**
     * Получение баланса
     */
    public static Balance balance() {
        ActionEnum actionEnum = ActionEnum.BALANCE;
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(2))
                        .GET()
                        .build();

        Balance balance = null;
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse != null) {
                LoggerUtil.logRequestResponse(actionEnum, "", httpResponse);
                if (httpResponse.statusCode() == 200) {
                    MapperUtils<Balance> mapper = new MapperUtils<>(Balance.class);
                    balance = mapper.convertToObject(httpResponse.body());
                }
            } else {
                LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

//            LoggerUtil.log(actionEnum, e.getMessage().substring(0, 40));
        }

        return balance;
    }

}
