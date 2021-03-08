package ru.bigint;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggerUtil {
    private static ActionEnum[] LOGGER_LEVEL = {ActionEnum.EXPLORE};

    public static void log(ActionEnum actionEnum, Object msg) {
        for (ActionEnum item: LOGGER_LEVEL) {
            if (item.equals(actionEnum) || item.equals(ActionEnum.ALL)) {
                System.out.println("Action: " + actionEnum + "; " + msg);
            }
        }
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }

    public static void logRequestResponse(ActionEnum actionEnum, Object requestObject, HttpResponse<String> httpResponse) {
        String strObj = null;
        if (requestObject instanceof String[]) {
            String[] arr = (String[]) requestObject;
            strObj = "[" + Arrays.stream(arr).collect(Collectors.joining()) + "]";
        } else if (requestObject instanceof Integer[]) {
            Integer[] arr = (Integer[]) requestObject;
            strObj = "[" + Arrays.stream(arr).map(item -> String.valueOf(item)).collect(Collectors.joining(", ")) + "]";
        } else {
            strObj = requestObject.toString();
        }

        //ToDo: only errors
        if ( httpResponse.statusCode() != 200 ) {
            log(actionEnum, "Request object: " + strObj + "; Response Code: " + httpResponse.statusCode() + "; Body: " + httpResponse.body());
        }
    }
}
