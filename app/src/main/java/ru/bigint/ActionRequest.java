package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        Logger.log(actionEnum, ">>> Request to: " + actionEnum + "; Object = " + licenseRequest);


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


    public static License license() throws IOException, InterruptedException {
//        Logger.log("-- Licence post --");
//        String body = Request.doGet(RequestEnum.LICENSES);
//        MapperUtils<License> mapper = new MapperUtils<>(License.class);
//        License license = mapper.convertToObject(body);
//        return license;
        return null;
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
                //Logger.log(actionEnum, "<<< Response: " + actionEnum + "; Retry: " + retry + "; Response = null: " + response);
            }

            retry++;
            if (response != null && (response.statusCode() == 200 || response.statusCode() == 404)) {
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


    public static int[] cash(String treasure) throws IOException, InterruptedException {
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

        int[] money = null;
        if (response != null && response.statusCode() == 200) {
            MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
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


    public static Balance balance() throws IOException, InterruptedException {
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

}