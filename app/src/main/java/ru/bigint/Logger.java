package ru.bigint;

public class Logger {
    private static ActionEnum[] LOGGER_LEVEL = {ActionEnum.EXPLORE};

    public static void log(ActionEnum actionEnum, Object msg) {
        for (ActionEnum item: LOGGER_LEVEL) {
            if (item.equals(actionEnum) || item.equals(ActionEnum.ALL)) {
                System.out.println(msg);
            }
        }
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }

}