package ru.bigint;

public class Constant {
    public final static String version = "hardcode_get_point_and_money";

    //Общий размер карты [0..3499]
    public final static int mapSize = 3499;

    //Число потоков для раскопок
    public final static int threadsCountDig = 30;

    //Число потоков для просмотра карты
    public final static int threadsCountExplore = 30;

    public final static int threadsCountLicense = 10;

    public final static int threadsCountCash = 40;

    //Максимальный X для просмотра карты построчно (по столбцам)
    public final static int maxExploreX = 20;

    //Разница при которой стоит копать это сокровище
    public final static int deltaDigCashTreasure = 15;

    //какую область стоит рассматривать для поиска, не меннее сколько сикровищ олжно в ней быть
    public final static int goodExploreAreaCount = 50;

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
