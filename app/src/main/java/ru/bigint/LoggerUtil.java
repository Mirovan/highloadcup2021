package ru.bigint;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoggerUtil {
    private static ActionEnum[] LOGGER_LEVEL = {ActionEnum.LICENSES, ActionEnum.DIG};

    public static void log(ActionEnum actionEnum, Object msg) {
        for (ActionEnum item: LOGGER_LEVEL) {
            if (item.equals(actionEnum) || item.equals(ActionEnum.ALL)) {
                System.out.println("Action: " + actionEnum + "; Log: " + msg);
            }
        }
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }

    public static void logRequestResponse(ActionEnum actionEnum, HttpRequest httpRequest, HttpResponse<String> httpResponse) {
        System.out.println("Action: " + actionEnum + "; Request: " + httpRequest.bodyPublisher().get() + "; Response Code: " + httpResponse.statusCode() + "; Body: " + httpResponse.body());
    }
}
