package ru.bigint;

public class Logger {
    private static RequestAction[] LOGGER_LEVEL = {RequestAction.DIG, RequestAction.CASH};

    public static void log(RequestAction requestAction, Object msg) {
        for (RequestAction item: LOGGER_LEVEL) {
            if (item.equals(requestAction) || item.equals(RequestAction.ALL)) {
                System.out.println(msg);
            }
        }
    }

    public static void log(Object msg) {
        if (LOGGER_LEVEL.equals(RequestAction.ALL)) {
            System.out.println(msg);
        }
    }

}
