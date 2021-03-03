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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ActionRequest {

    public static Explore explore(ExploreRequest exploreRequest) throws IOException, InterruptedException {
//        Logger.log("-- Explore --");
        ActionEnum actionEnum = ActionEnum.EXPLORE;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum + "; Object = " + exploreRequest);


        HttpResponse<String> response;
        int retry = 1;
        do {
            response = ClientRequest.doPost(actionEnum, exploreRequest);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && response.statusCode() == 200) {
                break;
            }
        } while (retry < Constant.retryCount);

        Explore explore = null;
        if (response != null && response.statusCode() == 200) {
            MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);
            explore = mapper.convertToObject(response.body());
        }

        return explore;
    }


    public static License license(int[] licenseRequest) throws IOException, InterruptedException {
//        Logger.log("-- Licence post --");
        ActionEnum actionEnum = ActionEnum.LICENSES;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum + "; Object = " + Arrays.toString(licenseRequest));


        HttpResponse<String> response;
        int retry = 1;
        do {
            response = ClientRequest.doPost(actionEnum, licenseRequest);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && response.statusCode() == 200) {
                break;
            }
        } while (retry < Constant.retryCount);

        License license = null;
        if (response != null && response.statusCode() == 200) {
            MapperUtils<License> mapper = new MapperUtils<>(License.class);
            license = mapper.convertToObject(response.body());
        }

        return license;
    }


    public static License[] license() {
//        Logger.log("-- Licence post --");
        ActionEnum actionEnum = ActionEnum.LICENSES;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum);

        MapperUtils<License[]> mapper = new MapperUtils<>(License[].class);
        HttpResponse<String> response = null;

        int retry = 1;
        do {
            response = ClientRequest.doGet(ActionEnum.LICENSES);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && response.statusCode() == 200) {
                break;
            }
        } while (retry < Constant.retryCount);

        License[] licenses = null;
        if (response != null) {
            licenses = mapper.convertToObject(response.body());
        }
        return licenses;
    }


    public static String[] dig(DigRequest digRequest) throws IOException, InterruptedException {
//        Logger.log("-- Dig --");
        ActionEnum actionEnum = ActionEnum.DIG;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum + "; Object = " + digRequest);

        HttpResponse<String> response;
        int retry = 1;
        do {
            response = ClientRequest.doPost(actionEnum, digRequest);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null
                    && (response.statusCode() == 200 || response.statusCode() == 404 || response.statusCode() == 403)) {
                break;
            }
        } while (retry < Constant.retryCount);

        String[] dig = null;
        if (response != null) {
            if (response.statusCode() == 200) {
                MapperUtils<String[]> mapper = new MapperUtils<>(String[].class);
                dig = mapper.convertToObject(response.body());
            } else if (response.statusCode() == 404) {
                dig = new String[0];
            }
        }
        return dig;
    }


    public static Integer[] cash(String treasure) throws IOException, InterruptedException {
//        Logger.log("-- Cash --");
        ActionEnum actionEnum = ActionEnum.CASH;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum + "; Object = " + treasure);

        HttpResponse<String> response;
        int retry = 1;
        do {
            response = ClientRequest.doPost(actionEnum, treasure);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && response.statusCode() == 200) {
                break;
            }
        } while (retry < Constant.retryCount);

        Integer[] money = null;
        if (response != null && response.statusCode() == 200) {
            MapperUtils<Integer[]> mapper = new MapperUtils<>(Integer[].class);
            money = mapper.convertToObject(response.body());
        }

        return money;
    }


    public static String healthCheck() throws IOException, InterruptedException {
//        String body = Request.doGet(RequestAction.HEALTH_CHECK);
//        Logger.log(body);
//        return body;
        return null;
    }


    public static Balance balance() throws IOException, InterruptedException, ExecutionException {
        ActionEnum actionEnum = ActionEnum.BALANCE;
        Logger.log(actionEnum, ">>> Request to: " + actionEnum);

        HttpResponse<String> response;
        int retry = 1;
        do {
            ClientRequest clientRequest = new ClientRequest();
            response = clientRequest.doGet(actionEnum);
            if (response != null) {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
            } else {
                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && response.statusCode() == 200) {
                break;
            }
        } while (retry < Constant.retryCount);

        Balance balance = null;
        if (response != null && response.statusCode() == 200) {
            MapperUtils<Balance> mapper = new MapperUtils<>(Balance.class);
            balance = mapper.convertToObject(response.body());
        }

        return balance;
    }


    public static List<DigWrapper> dig(List<Point> points, List<License> licenses) {
        ActionEnum actionEnum = ActionEnum.DIG;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        HttpClient httpClient = HttpClient.newBuilder().build();

        //Формируем запросы по числу лицензий
        List<CompletableFuture<DigWrapper>> listCf = new ArrayList<>();
        for (int i = 0; i < licenses.size() && i < points.size(); i++) {
            Point point = points.get(i);
            License license = licenses.get(i);
            DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth());

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = null;
            try {
                requestBody = objectMapper.writeValueAsString(digRequest);
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
//                            if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 404 || httpResponse.statusCode() == 403)) {
                            if (httpResponse.statusCode() == 200) {
                                MapperUtils<String[]> resultMapper = new MapperUtils<>(String[].class);
                                treasures = resultMapper.convertToObject(httpResponse.body());
                                Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Response code: " + httpResponse.statusCode() + "; Response body: " + httpResponse.body());
                            }
                        } else {
                            Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                        }

                        return new DigWrapper(digRequest, treasures);
                    });

            listCf.add(cf);
        }

        List<DigWrapper> res = null;

        try {
            res = new ArrayList<>();
            for (int i=0; i<listCf.size(); i++) {
                DigWrapper digWrapper = null;
                try {
                    digWrapper = listCf.get(i).get();
                } catch (InterruptedException | ExecutionException e) {
                    Logger.log("DIG collect = " + e.getMessage());
                }
                res.add(digWrapper);
            }

//            res = listCf.stream().map(item -> {
//                DigWrapper digWrapper = null;
//                try {
//                    digWrapper = item.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//                return digWrapper;
//            }).collect(Collectors.toList());
        } catch (NullPointerException e) {
            Logger.log(e.getMessage());
        }

        return res;
    }

}