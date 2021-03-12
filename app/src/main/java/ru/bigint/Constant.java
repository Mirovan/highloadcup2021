package ru.bigint;

public class Constant {
    public final static String version = "stage2_006_multidig";

    //Общий размер карты
    public final static int mapSize = 3499;
    //Размер карты для просмотра
    public final static int areaSize = 300;

    //Число потоков
    public final static int threadsCount = 10;

    //Число потоков для раскопок
    public final static int threadsCountDig = 30;

    //Число потоков для просмотра карты
    public final static int threadsCountExplore = 30;

    //Максимальный X для просмотра карты построчно (по столбцам)
    public final static int maxExploreX = 60;

    //Максимальное число лицензий (по условию может быть только 10 активных лицензий)
    public final static int maxLicencesCount = 10;

    //Число денег которые тратим на покупку лицензии
    public final static int licensePaymentCount = 1;

    //Число платных лицензий которые можно запросить за один запрос
    public final static int paidLicensesCount = 10;

    //Число попыток для повторных неуспешных запросов
    public static int retryCount = 2;

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
