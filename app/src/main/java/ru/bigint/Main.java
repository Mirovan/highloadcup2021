package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

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
            List<Point> points = Actions.getPoints(line);
            LoggerUtil.log("Line: " + line + "; Points with treasures: " + points.size());

            ConcurrentLinkedQueue<Point> pointStack = new ConcurrentLinkedQueue<>(points);

            //Пока стек с точками не пустой
            while (!pointStack.isEmpty()) {
                //Обновляем лицензии
                Actions.updateLicenses(client);

                //List - коллекция для последующего исследования (после раскопок), stack - используем для понимания в каких точках копаем
                List<Point> digPoints = new ArrayList<>();
                ConcurrentLinkedQueue<Point> digPointsStack = new ConcurrentLinkedQueue<>();
                int digCount = client.getLicenses().stream()
                        .reduce(0, (res, item) -> res + (item.getDigAllowed()-item.getDigUsed()), Integer::sum);

                for (int i = 0; i < digCount; i++) {
                    if (!pointStack.isEmpty()) {
                        Point point = pointStack.poll();
                        digPoints.add(point);
                        digPointsStack.add(point);
                    }
                }

                //копаем
                List<DigWrapper> digs = Actions.dig(client.getLicenses(), digPointsStack);

                for (DigWrapper dig: digs) {
                    //Если что-то выкопали (может и пустое)
                    if (dig.getTreasures() != null) {
                        int licId = dig.getDigRequest().getLicenseID();
                        //Обновляем лицензию в общем списке
                        License license = client.getLicenses().stream()
                                .filter(item -> item.getId() == licId)
                                .findFirst()
                                .get();
                        license.setDigUsed(license.getDigUsed() + 1);

                        //находим точку в списке и обновляем её
                        Point point = digPoints.stream()
                                .filter(item -> item.getX() == dig.getDigRequest().getPosX()
                                        && item.getY() == dig.getDigRequest().getPosY())
                                .findFirst()
                                .get();
                        point.setDepth(point.getDepth() + 1);
                        point.setTreasuresCount(point.getTreasuresCount() - dig.getTreasures().length);

                        //Помещаем обратно точку в стек - если сокровища еще есть
                        if (point.getTreasuresCount() > 0) {
                            pointStack.add(point);
                        }

                        //Сохраняем в коллекцию сокровища
                        treasures.addAll(Arrays.asList(dig.getTreasures()));
                    }
                }

                List<Integer> money = Actions.cash(treasures);
                client.getMoney().addAll(money);
            }

        }

        LoggerUtil.log("FINISH");

    }

}