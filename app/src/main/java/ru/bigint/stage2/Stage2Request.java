package ru.bigint.stage2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.ActionEnum;
import ru.bigint.Constant;
import ru.bigint.LoggerUtil;
import ru.bigint.MapperUtils;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Stage2Request {

    private static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    public static Explore explore(ExploreRequest exploreRequest) {
        ActionEnum actionEnum = ActionEnum.EXPLORE;

        String url = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(exploreRequest);
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
                    Explore explore = null;
                    if (httpResponse != null) {
                        LoggerUtil.logRequestResponse(actionEnum, httpRequest, httpResponse);
                        if (httpResponse.statusCode() == 200) {
                            MapperUtils<Explore> resultMapper = new MapperUtils<>(Explore.class);
                            explore = resultMapper.convertToObject(httpResponse.body());
                        }
                    } else {
                        LoggerUtil.log(actionEnum, "<<< Response: " + actionEnum + "; Response = null");
                    }

                    return explore;
                });


        Explore res = null;
        try {
            res = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.log(actionEnum, e.getMessage());
        }

        return res;
    }
}
