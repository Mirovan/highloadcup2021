package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;

public class Main {
    private final static String address = System.getenv("ADDRESS");
    private final static String port = "8000";
    private final static String schema = "http";
    private final static String URI = schema + "://" + address + ":" + port;

//  private final static String URI = "http://localhost:8080";
//  private final static String URI = "http://192.168.1.176:8080";

    private final static int areaSize = 3500;
    private final static int maxDepth = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
        Logger.log("--- Running App ---");

        long startTime = System.currentTimeMillis();

        Main main = new Main();

//        Thread.sleep(5000);
        main.runGame();

        Logger.log("Time: " + (System.currentTimeMillis() - startTime));
//        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }

    private void runGame() throws IOException, InterruptedException {
        Logger.log("--- Play Game ---");

        int resMoney = 0;

        Logger.log("OS: " + System.getProperty("os.name"));
        Logger.log("URI: " + URI);

//        RequestEndpoint.healthCheck(URI);

        Client client = new Client();

        for (int i = 1; i < areaSize; i++) {
            for (int j = 1; j < areaSize; j++) {
                int x = i;
                int y = j;
                Logger.log("--- Next point ---");
                Logger.log("x = " + x + "; y = " + y);
//                System.out.println("x = " + x + "; y = " + y);

                //Сколько золота есть в точке
                ExploreRequest exploreRequest = new ExploreRequest(x, y, 1, 1);
                Explore explore = RequestEndpoint.explore(URI, exploreRequest);

                int treasureCount = explore.getAmount();
                //Пока есть сокровища и глубина позволяет - копать
                int depth = 1;
                while (treasureCount > 0 && depth <= maxDepth) {
                    //Проверка - если нет лицензии на раскопки или нельзя копать - то надо получить лицензию
                    if (client.getLicense() == null
                            || client.getLicense().getDigUsed() >= client.getLicense().getDigAllowed()) {
                        License license = RequestEndpoint.postLicense(URI, new int[]{});
                        client.setLicense(license);
                    }

                    //Если можно копать
                    if (client.getLicense() != null && client.getLicense().getDigUsed() < client.getLicense().getDigAllowed()) {
                        //копаем - и находим список сокровищ на уровне
                        DigRequest digRequest = new DigRequest(client.getLicense().getId(), x, y, depth);
                        String[] treasures = RequestEndpoint.dig(URI, digRequest);

                        //изменяем число попыток раскопок и текущую глубину
                        client.getLicense().setDigUsed(client.getLicense().getDigUsed() + 1);
                        depth++;
                        //Изменяем число сокровищ для координаты x,y
                        if (treasures != null) treasureCount -= treasures.length;

                        //Меняем сокровища на золото
                        for (String treasure : treasures) {
                            int[] money = RequestEndpoint.cash(URI, treasure);
                            resMoney += money[0];
                        }
                    }
                }


            }
        }

        Logger.log("=================================");
        Logger.log("Result : " + resMoney);
    }

}