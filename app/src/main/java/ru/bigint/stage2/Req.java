package ru.bigint.stage2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.ActionEnum;
import ru.bigint.Constant;
import ru.bigint.LoggerUtil;
import ru.bigint.MapperUtils;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.response.License;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Req {
    private static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();


    public static DigWrapper dig(DigRequest requestObj, List<License> licenses) {
        ActionEnum actionEnum = ActionEnum.DIG;
        String url = Constant.SERVER_URI + actionEnum.getRequest();

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

        DigWrapper res = null;
        try {
            res = httpClient
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        String[] treasures = null;
                        if (httpResponse != null) {
                            License lic = licenses.stream()
                                    .filter(item -> item.getId() == requestObj.getLicenseID())
                                    .findFirst()
                                    .get();
                            //System.out.println(lic + "; " + requestObj.getLicenseID() + "; " + requestObj + "; Response Code: " + httpResponse.statusCode() + "; Body: " + httpResponse.body());
                            LoggerUtil.logRequestResponse(actionEnum, " lic=" + lic.toString() + "; requestObj=" + requestObj.toString(), httpResponse);

//                            if (requestObj.getLicenseID() >= 7 && requestObj.getLicenseID() <= 10) {
//                            System.out.println(lic + "; " + requestObj.getLicenseID() + "; " + requestObj + "; Response Code: " + httpResponse.statusCode() + "; Body: " + httpResponse.body());
//                            }

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
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

}
