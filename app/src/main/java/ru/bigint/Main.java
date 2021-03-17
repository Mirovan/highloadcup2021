package ru.bigint;

import ru.bigint.hardcode.Hardcode;
import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        LoggerUtil.log("--- VERSION : " + Constant.version + " ---");
        Main main = new Main();
        main.runGame();
        System.out.println( (float) (System.currentTimeMillis() - time));
    }

    private void runGame() {
        Client client = new Client();
        client.setLicenses(new ArrayList<>());
        client.setMoney(new ArrayList<>());

        //получаем все точки с сокровищами
        List<Point> points = Hardcode.getPoints();
//        List<Point> points = Actions.explorePoints(52);
        LoggerUtil.log("Points with treasures: " + points.size());


        Stack<Point> pointStack = new Stack<>();
        pointStack.addAll(points);

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

                    //Помещаем обратно точку в стек - если не дошли до нужного уровня
                    if (point.getDepth() < point.getTreasureDepth()) {
                        pointStack.add(point);
                    } else {
                        //Сохраняем в коллекцию сокровища
                        for (String treasure : dig.getTreasures()) {
                            treasures.add(treasure);
                        }
                    }
                }
            }

            List<Integer> money = Actions.cash(treasures);
            client.getMoney().addAll(money);

        }


        LoggerUtil.log("FINISH");

    }

}