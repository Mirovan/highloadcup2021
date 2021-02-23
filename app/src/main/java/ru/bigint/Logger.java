package ru.bigint;

public class Logger {
    private static RequestEnum LOGGER_LEVEL = RequestEnum.DIG;

    public static void log(RequestEnum requestEnum, Object msg) {
        if (requestEnum.equals(LOGGER_LEVEL) || LOGGER_LEVEL.equals(RequestEnum.ALL)) {
            System.out.println(msg);
        }
    }

    public static void log(Object msg) {
        if (LOGGER_LEVEL.equals(RequestEnum.ALL)) {
            System.out.println(msg);
        }
    }

}
