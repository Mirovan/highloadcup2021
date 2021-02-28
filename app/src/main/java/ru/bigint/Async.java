package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.Client;
import ru.bigint.model.DigRequest;
import ru.bigint.model.License;
import ru.bigint.model.Point;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Async {

    public static CompletableFuture<License> getLicense(Client client, int[] arr) throws ExecutionException, InterruptedException {
        if (client != null && client.getLicenses() != null
                && client.getLicenses().get(0).getDigUsed() < client.getLicenses().get(0).getDigAllowed()) {
            return CompletableFuture.completedFuture(client.getLicenses().get(0));
        } else {
            MapperUtils<License> licenseMapperUtils = new MapperUtils<>(License.class);

            return getNewLicense(client, arr)
                    .thenApply(HttpResponse::body)
                    .thenApply(licenseMapperUtils::convertToObject);
        }
    }

    public static CompletableFuture<HttpResponse<String>> getNewLicense(Client client, int[] arr) throws ExecutionException, InterruptedException {
        ActionEnum actionEnum = ActionEnum.LICENSES;
        String exploreURL = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(arr);
        } catch (JsonProcessingException e) {
//                                Logger.log(e.getMessage());
        }

        HttpRequest exploreRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(exploreURL))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        CompletableFuture<HttpResponse<String>> cf = httpClient
                .sendAsync(exploreRequest, HttpResponse.BodyHandlers.ofString());

        return cf;
    }


    public static CompletableFuture<HttpResponse<String>> dig(DigRequest digRequest) {
        ActionEnum actionEnum = ActionEnum.DIG;
        String exploreURL = Constant.SERVER_URI + actionEnum.getRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(digRequest);
        } catch (JsonProcessingException e) {
//                                Logger.log(e.getMessage());
        }

        HttpRequest exploreRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(exploreURL))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        CompletableFuture<HttpResponse<String>> cf = httpClient
                .sendAsync(exploreRequest, HttpResponse.BodyHandlers.ofString());

        return cf;
    }

}
