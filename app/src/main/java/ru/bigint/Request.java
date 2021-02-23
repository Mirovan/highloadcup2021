package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.ExploreRequest;

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

public class Request {

    private static String SERVER_ADDRESS = "localhost";
    static {
        if (System.getenv("ADDRESS") != null) {
            SERVER_ADDRESS = System.getenv("ADDRESS");
        }
    }
    private final static String SERVER_PORT = "8000";
    private final static String SERVER_SCHEMA = "http";
    private final static String SERVER_URI = SERVER_SCHEMA + "://" + SERVER_ADDRESS + ":" + SERVER_PORT;

    private static int retryCount = 5;

    private static HttpClient httpClient = HttpClient.newBuilder()
//            .version(HttpClient.Version.HTTP_2)
//            .connectTimeout(Duration.ofSeconds(10))
            .build();


    public static String doGet(RequestEnum requestEnum) throws IOException, InterruptedException {
        String url = SERVER_URI + requestEnum.getRequest();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
//                        .timeout(Duration.ofSeconds(2))
                        .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String doPost(RequestEnum requestEnum, Object body) throws IOException, InterruptedException {
        String url = SERVER_URI + requestEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(body);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .timeout(Duration.ofSeconds(1))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        HttpResponse<String> response = null;
        String responseBody = null;
        int retry = 1;
        do {
//            CompletableFuture<HttpResponse<String>> cf =
//                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            CompletableFuture<HttpResponse<String>> cf =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            try {
                response = cf.get();
                responseBody = response.body();
                Logger.log(requestEnum, "URL: " + requestEnum.getRequest() + "; Retry: " + retry + "; Response code: " + response.statusCode() + "; Response body: " + response.body());
//                Logger.log("Response body: " + response.body());
            } catch (ExecutionException e) {
//                Logger.log(e.getMessage());
            }

            retry++;
            if (response != null && response.statusCode() == 200) break;
        } while (retry < retryCount);

        return responseBody;
    }


    public static List<CompletableFuture<String>> concurrentCalls(RequestEnum requestEnum, final List<ExploreRequest> requestList) {
        String url = SERVER_URI + requestEnum.getRequest();

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

                            CompletableFuture<String> res = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                    .thenApply(response -> {
//                                        Logger.log("Response code: " + response.statusCode());
                                        return response;
                                    })
                                    .thenApply(HttpResponse::body);
                            return res;
                        }
                )
                .collect(Collectors.toList());
    }

}
