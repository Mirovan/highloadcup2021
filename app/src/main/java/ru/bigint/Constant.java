package ru.bigint;

public class Constant {
    //Размер карты для просмотра
    public final static int areaSize = 270;

    //Число потоков
    public final static int threadsCount = 10;

    //Число попыток для повторных неуспешных запросов
    public static int retryCount = 5;

    private static String SERVER_ADDRESS = "localhost";
    static {
        if (System.getenv("ADDRESS") != null) {
            SERVER_ADDRESS = System.getenv("ADDRESS");
        }
    }
    private final static String SERVER_PORT = "8000";
    private final static String SERVER_SCHEMA = "http";
    public final static String SERVER_URI = SERVER_SCHEMA + "://" + SERVER_ADDRESS + ":" + SERVER_PORT;
}