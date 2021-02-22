package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    private final static String address = System.getenv("ADDRESS");
    private final static String port = "8000";
    private final static String schema = "http";
    private final static String URI = schema + "://" + address + ":" + port;

//  private final static String URI = "http://localhost:8080";
//  private final static String URI = "http://192.168.1.176:8080";

    private final static int areaSize = 3500;
    private final static int maxDepth = 10;

    private final static int threadsCount = 5;


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

        for (int x = 1; x < areaSize; x++) {
            int y = 1;
            while (y < areaSize) {
                int tempY = y;

                //Делаем threadsCount-число асинхронных запросов на запрос /explore
                List<ExploreRequest> requestList = new ArrayList<>();
                for (int k = 0; k < threadsCount && y < areaSize; k++) {
                    ExploreRequest exploreRequest = new ExploreRequest(x, y, 1, 1);
                    requestList.add(exploreRequest);
                    y++;
                }


                //Получаем результаты асинхронных запросов
                List<CompletableFuture<String>> cfReponseList = Request.concurrentCalls(URI+"/explore", requestList);
                for (int i = 0; i < cfReponseList.size(); i++) {
                    CompletableFuture<String> response = cfReponseList.get(i);
                    String responseBody = null;
                    try {
                        Logger.log("--- Next point ---");
                        Logger.log("x = " + x + "; y = " + (tempY+i));

                        responseBody = response.get();
                        Logger.log("Response: " + responseBody);

                        MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);
                        Explore explore = mapper.convertToObject(responseBody);

                        if (explore != null) {
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

                                    if (treasures != null) {
                                        //Изменяем число сокровищ для координаты x,y
                                        treasureCount -= treasures.length;

                                        //Меняем сокровища на золото
                                        for (String treasure : treasures) {
                                            int[] money = RequestEndpoint.cash(URI, treasure);
                                            if (money != null && money.length > 0) {
                                                resMoney += money[0];
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    } catch (ExecutionException e) {
                        Logger.log(e.getMessage());
                    }
                }

            }
        }

        Logger.log("=================================");
        Logger.log("Result : " + resMoney);
    }

}