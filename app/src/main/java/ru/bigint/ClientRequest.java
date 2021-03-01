package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class ClientRequest<T> {

    private static HttpClient httpClient = HttpClient.newBuilder()
//            .version(HttpClient.Version.HTTP_2)
//            .connectTimeout(Duration.ofSeconds(10))
            .build();


    public static HttpResponse<String> doGet(ActionEnum actionEnum) throws IOException, InterruptedException, ExecutionException {
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
//                        .timeout(Duration.ofSeconds(2))
                        .build();
        HttpResponse<String> response =
                httpClient
                        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .get();
        return response;
    }


    public static HttpResponse<String> doPost(ActionEnum actionEnum, Object requestObject) throws IOException, InterruptedException {
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestObject);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(1))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        HttpResponse<String> response = null;
        CompletableFuture<HttpResponse<String>> cf =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        try {
            response = cf.get();
//                Logger.log("Response body: " + response.body());
        } catch (ExecutionException e) {
//                Logger.log(e.getMessage());
        }


        return response;
    }


    public List<CompletableFuture<HttpResponse<String>>> concurrentPost(ActionEnum actionEnum, final List<T> requestList) {
        String url = Constant.SERVER_URI + actionEnum.getRequest();

        return requestList.stream()
                .map(item -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            String requestBody = null;
                            try {
                                requestBody = objectMapper.writeValueAsString(item);
                            } catch (JsonProcessingException e) {
//                                Logger.log(e.getMessage());
                            }

                            HttpRequest request =
                                    HttpRequest.newBuilder()
                                            .uri(URI.create(url))
                                            .header("Content-Type", "application/json; charset=UTF-8")
//                                            .timeout(Duration.ofSeconds(5))
                                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                            .build();

                            CompletableFuture<HttpResponse<String>> res =
                                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                            return res;
                        }
                )
                .collect(Collectors.toList());
    }

}
