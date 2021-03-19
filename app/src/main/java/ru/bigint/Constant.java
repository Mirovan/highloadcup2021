package ru.bigint;

public class Constant {
    public final static String version = "task/test_new_async_requests";

    //Общий размер карты
    public final static int mapSize = 3499;

    //Число потоков для раскопок
    public final static int threadsCountDig = 40;

    //Число потоков для просмотра карты
    public final static int threadsCountExplore = 35;

    //Для Explore - Делим строку line на partSize-частей и для каждой этой части делаем бинарный поиск
    public final static int explorePartSize = 100;

    public final static int threadsCountLicense = 10;

    public final static int threadsCountCash = 40;

    //Максимальное число лицензий (по условию может быть только 10 активных лицензий)
    public final static int maxLicencesCount = 10;

    //Число денег которые тратим на покупку лицензии
    public final static int paidForLicense = 1;

    //число платных лилцензий
    public final static int paidlicenseCount = 7;

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
