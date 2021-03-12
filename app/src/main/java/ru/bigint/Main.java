package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.response.License;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Point> points = MultiRequest.getPoints();
        LoggerUtil.log("Points with treasures: " + points.size());

//        License license = null;

        Stack<Point> pointStack = new Stack<>();
        pointStack.addAll(points);

        int stop = 0;

        //Пока стек с точками не пустой
        while (!pointStack.empty()) {
            //Убираем истекшие лицензии
            List<License> licencesUpdate = new ArrayList<>();
            for (License lic: client.getLicenses()) {
                if (lic.getDigUsed() < lic.getDigAllowed()) {
                    licencesUpdate.add(lic);
                }
            }
            client.setLicenses(licencesUpdate);

            //Получаем новые лицензии - в одном потоке/синхронно - пока все лицензии не получим
            List<License> licensesNew = new ArrayList<>();
            for (int i = client.getLicenses().size(); i < Constant.maxLicencesCount; i++) {
                Integer[] money = new Integer[]{};
                //Формируем список монет для получения платной лицензии
                if (client.getMoney().size() >= Constant.licensePaymentCount) {
                    money = new Integer[Constant.licensePaymentCount];
                    for (int j = 0; j < Constant.licensePaymentCount; j++) {
                        money[j] = client.getMoney().get(0);
                        client.getMoney().remove(0);
                    }
                }

                License license;
                do {
                    license = SimpleRequest.license(money);
                } while (license == null);

                if (license != null) licensesNew.add(license);
            }
            client.getLicenses().addAll(licensesNew);


            //List - коллекция для последующего исследования (после раскопок), stack - используем для понимания в каких точках копаем
            List<Point> digPoints = new ArrayList<>();
            Stack<Point> digPointsStack = new Stack<>();
            int digPointCount = client.getLicenses().stream()
                    .reduce(0, (res, item) -> res + (item.getDigAllowed()-item.getDigUsed()), Integer::sum);

            for (int i = 0; i < digPointCount; i++) {
                if (!pointStack.empty()) {
                    Point point = pointStack.pop();
                    digPoints.add(point);
                    digPointsStack.add(point);
                }
            }


            //копаем
//            System.out.println(" ########### DIG ############ ");
            List<DigWrapper> digs = MultiRequest.dig(client.getLicenses(), digPointsStack);

            for (DigWrapper dig: digs) {
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

                    //Меняем сокровища
                    for (String treasure : dig.getTreasures()) {
                        Integer[] cash;
                        do {
                            cash = SimpleRequest.cash(treasure);
                        } while (cash == null);
                        client.getMoney().addAll(Arrays.asList(cash));
                    }
                }
            }

            stop++;
//            if (stop >= 4) break;
//            System.out.println(" ####################### ");
        }


    }

}