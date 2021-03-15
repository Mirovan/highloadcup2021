package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        List<Point> points = Actions.explorePoints();
        LoggerUtil.log("Points with treasures: " + points.size());

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
//                if (dig.getDigRequest().getLicenseID() >= 7 && dig.getDigRequest().getLicenseID() <= 10)
//                System.out.println("!!! - Dig: " + dig);

                //Если что-то выкопали (может и пустое)
                if (dig.getTreasures() != null) {
                    int licId = dig.getDigRequest().getLicenseID();
                    //Обновляем лицензию в общем списке
                    License license = client.getLicenses().stream()
                            .filter(item -> item.getId() == licId)
                            .findFirst()
                            .get();
//                    if (license.getId() >= 7 && license.getId() <= 10) System.out.println("lic before dig: " + license);
                    license.setDigUsed(license.getDigUsed() + 1);
//                    if (license.getId() >= 7 && license.getId() <= 10) System.out.println("lic after dig: " + license);

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
                        treasures.add(treasure);
//                        Integer[] cash;
//                        do {
//                            cash = SimpleRequest.cash(treasure);
//                        } while (cash == null);
//                        client.getMoney().addAll(Arrays.asList(cash));
                    }
                }
            }

            List<Integer> money = Actions.cash(treasures);
            client.getMoney().addAll(money);

//            stop++;
//            if (stop >= 4) break;
//            System.out.println(" ####################### ");
        }

        LoggerUtil.log("FINISH");

    }

}