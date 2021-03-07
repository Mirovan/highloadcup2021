package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;
import ru.bigint.stage2.AlgoUtils;
import ru.bigint.stage2.Stage2Request;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

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

        License license = null;

        int tres = 0;
        for (int x = 1; x < Constant.mapSize; x++) {
            AlgoUtils.iteration = 0;
            List<Point> list = AlgoUtils.binSearch(x, 1, 3499);
            System.out.println("x=" + x + "; iteration: " + AlgoUtils.iteration);
            Integer tresByX = list.stream().map(Point::getTreasuresCount).reduce(0, (a, b) -> a + b);
            tres = tres + tresByX;
            System.out.println("tresByX: " + tresByX + "; allTres = " + tres);

/*            for (int y = 1; y < Constant.mapSize; y++) {

                Explore explore = Stage2Request.explore(new ExploreRequest(x, y, 1, 1));

                if (explore != null && explore.getAmount() > 0) {
                    int pointTreasures = explore.getAmount();




                    int depth = 1;
                    while (pointTreasures > 0) {
                        if (license == null || license.getDigUsed() >= license.getDigAllowed()) {
                            Integer[] money = new Integer[]{};
                            //Формируем список монет для получения платной лицензии
                            if (client.getMoney().size() >= Constant.licensePaymentCount) {
                                money = new Integer[Constant.licensePaymentCount];
                                for (int i=0; i<Constant.licensePaymentCount; i++) {
                                    money[i] = client.getMoney().get(0);
                                    client.getMoney().remove(0);
                                }
                            }

                            do {
                                license = Stage2Request.license(money);
                            } while (license == null);
                        }

                        String[] dig;
                        do {
                            DigRequest digRequest = new DigRequest(license.getId(), x, y, depth);
                            dig = Stage2Request.dig(digRequest);
                        } while (dig == null);

                        if (dig != null) {
                            license.setDigUsed(license.getDigUsed() + 1);
                            depth++;

                            for (String treasure : dig) {
                                Integer[] cash;
                                do {
                                    cash = Stage2Request.cash(treasure);
                                } while (cash == null);
                                client.getMoney().addAll(Arrays.asList(cash));
                                pointTreasures = pointTreasures - cash.length;
                            }
                        }
                    }
                }

            }*/
        }

    }

}