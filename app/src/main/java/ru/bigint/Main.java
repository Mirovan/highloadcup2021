package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;

public class Main {
//    private final String address = System.getProperty("ADDRESS");
    //private final String port = System.getProperty("Port");
    //private final String schema = System.getProperty("Schema");

    private final static String address = "localhost";
    private final static String port = "8080";
    private final static String schema = "http";

    private final static int areaSize = 3500;

    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        main.runGame();

//        String get = Request.doGet(URI + "/balance");

        //ExploreRequest exploreRequest = new ExploreRequest(1, 2, 3, 4);
        //String post = Request.doPost(URI + "/explore", exploreRequest);
        //System.out.println(post);

        //System.out.println("Hello");
    }

    private void runGame() throws IOException, InterruptedException {
        int resMoney = 0;
        String URI = schema + "://" + address + ":" + port;
        Client client = new Client();

        for (int i = 0; i < areaSize; i++) {
            for (int j = 0; j < areaSize; j++) {
                int x = i;
                int y = j;
                Logger.log("x = " + x + "; y = " + y);

                //Сколько золота есть в точке
                ExploreRequest exploreRequest = new ExploreRequest(x, y, 0, 0);
                Explore explore = RequestEndpoint.explore(URI, exploreRequest);

                int treasureCount = explore.getAmount();
                //Пока есть сокровища и глубина позволяет - копать
                int depth = 1;
                while (treasureCount > 0 && depth <= 10) {
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
                        treasureCount -= treasures.length;

                        //Меняем сокровища на золото
                        for (String treasure: treasures) {
                            int[] money = RequestEndpoint.cash(URI, treasure);
                            resMoney += money[0];
                        }
                    }
                }

            }
        }

        System.out.println(resMoney);
    }

}