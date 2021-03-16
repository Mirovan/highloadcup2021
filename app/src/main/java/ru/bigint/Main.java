package ru.bigint;

import ru.bigint.hardcode.Hardcode;
import ru.bigint.model.CashWrapper;
import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        LoggerUtil.log("--- VERSION : " + Constant.version + " ---");
        Main main = new Main();
        main.runGame();
    }

    private void runGame() {
        Client client = new Client();
        client.setLicenses(new ArrayList<>());
        client.setMoney(new ArrayList<>());

        //получаем все точки с сокровищами
//        List<Point> points = Hardcode.getPoints();
        List<Point> points = Actions.explorePoints(0);
        LoggerUtil.log("Points with treasures: " + points.size());

        Set<Point> resSet = new TreeSet<>();


        Stack<Point> pointStack = new Stack<>();
        pointStack.addAll(points);


        int stop = 0;

        List<String> treasures = new ArrayList<>();

        //Пока стек с точками не пустой
        while (!pointStack.empty()) {
            //Обновляем лицензии
            Actions.updateLicenses(client);

            //List - коллекция для последующего исследования (после раскопок), stack - используем для понимания в каких точках копаем
            List<Point> digPoints = new ArrayList<>();
            Stack<Point> digPointsStack = new Stack<>();
            int digPointCount = client.getLicenses().stream()
                    .reduce(0, (res, item) -> res + (item.getDigAllowed() - item.getDigUsed()), Integer::sum);

            for (int i = 0; i < digPointCount; i++) {
                if (!pointStack.empty()) {
                    Point point = pointStack.pop();
                    digPoints.add(point);
                    digPointsStack.add(point);
                }
            }

            //копаем
            List<DigWrapper> digs = Actions.dig(client.getLicenses(), digPointsStack);

            for (DigWrapper dig : digs) {
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
                    for (String treasure : dig.getTreasures()) {
                        CashWrapper cash;
                        do {
                            cash = SimpleRequest.cash(treasure);
                        } while (cash == null);

                        if (cash.getResponse() != null) {
                            client.getMoney().addAll(Arrays.asList(cash.getResponse()));

                            //Если разница между числом заработанных денег при обмене сокровища и глубине больше N
                            if (cash.getResponse().length - point.getDepth() > 5) {
                                resSet.add(point);
                            }
                        }
                    }
                }
            }

        }


//        for (int i=0; i<resSet.toArray().length; i++) {
//            stRes += point.getX() + "," + point.getY() + "-" + point.getDepth() + ";";
//        }

        String stRes = resSet.stream()
                .map(point -> point.getX() + "," + point.getY() + "-" + point.getDepth())
                .collect(Collectors.joining(";"));
        System.out.println(stRes);

        LoggerUtil.log("FINISH");

    }

}