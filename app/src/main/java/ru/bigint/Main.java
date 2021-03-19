package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        LoggerUtil.log("--- VERSION : " + Constant.version + " ---");
        Main main = new Main();
        main.runGame();
        System.out.println( (float) (System.currentTimeMillis() - time) / 1000.0f );
    }

    private void runGame() {
        Client client = new Client();
        client.setLicenses(new ArrayList<>());
        client.setMoney(new ArrayList<>());

        List<String> treasures = new ArrayList<>();

        for (int line = 0; line < Constant.mapSize; line++) {
            //получаем все точки с сокровищами для линии line
            CopyOnWriteArraySet<Point> points = Actions.getPoints(line);
            LoggerUtil.log("Line: " + line + "; Points with treasures: " + points.size());

            ConcurrentLinkedQueue<Point> pointStack = new ConcurrentLinkedQueue<>(points);

            //Пока стек с точками не пустой
            while (!pointStack.isEmpty()) {
                //Обновляем лицензии
                Actions.updateLicenses(client);

                //копаем
                List<DigWrapper> digs = Actions.dig(client.getLicenses(), pointStack);

                for (DigWrapper dig: digs) {
                    //Если что-то выкопали (может и пустое)
                    if (dig.getResponse() != null) {
                        int licId = dig.getLicence().getId();
                        //Обновляем лицензию в общем списке
                        License license = client.getLicenses().stream()
                                .filter(item -> item.getId() == licId)
                                .findFirst()
                                .get();
                        license.setDigUsed(license.getDigUsed() + 1);

                        //обновляем точку
                        Point point = dig.getPoint();
                        point.setDepth(point.getDepth() + 1);
                        point.setTreasuresCount(point.getTreasuresCount() - dig.getResponse().length);

                        //Помещаем обратно точку в стек - если сокровища еще есть
                        if (point.getTreasuresCount() > 0) {
                            pointStack.add(point);
                        }

                        //Сохраняем в коллекцию сокровища
                        treasures.addAll(Arrays.asList(dig.getResponse()));
                    }
                }

                List<Integer> money = Actions.cash(treasures);
                client.getMoney().addAll(money);
            }

        }

        LoggerUtil.log("FINISH");

    }

}