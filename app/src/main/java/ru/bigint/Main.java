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
        main.runProcess();

//        String get = Request.doGet(URI + "/balance");

        //ExploreRequest exploreRequest = new ExploreRequest(1, 2, 3, 4);
        //String post = Request.doPost(URI + "/explore", exploreRequest);
        //System.out.println(post);

        //System.out.println("Hello");
    }

    private void runProcess() throws IOException, InterruptedException {
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

                int pointGoldCount = explore.getAmount();
                //Пока золото есть - копать
                int depth = 0;
                while (pointGoldCount > 0 || depth >= 10) {
                    //Проверка - если нет лицензии на раскопки или нельзя копать - то надо получить лицензию
                    if (client.getLicense() == null
                            || client.getLicense().getDigUsed() >= client.getLicense().getDigAllowed()) {
                        License license = RequestEndpoint.postLicense(URI, new int[]{});
                        client.setLicense(license);
                    }

                    //Если можно копать
                    if (client.getLicense() != null && client.getLicense().getDigUsed() < client.getLicense().getDigAllowed()) {
                        //копаем
                        DigRequest digRequest = new DigRequest(client.getLicense().getId(), x, y, 1);
                        String[] dig = RequestEndpoint.dig(URI, digRequest);

                        //изменяем число попыток раскопок и текущую глубину
                        client.getLicense().setDigUsed(client.getLicense().getDigUsed() + 1);
                        depth++;
                    }
                }

            }
        }

    }

}