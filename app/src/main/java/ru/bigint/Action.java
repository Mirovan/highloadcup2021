package ru.bigint;

import ru.bigint.model.*;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Balance;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class Action {

    public static License license(int[] arr) throws IOException, InterruptedException {
        return ActionRequest.license(arr);
    }


    public static String[] dig(License license, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), depth);
        String[] treasures = ActionRequest.dig(digRequest);
        return treasures;
    }


    public static Integer[] cash(String treasure) throws IOException, InterruptedException {
        return ActionRequest.cash(treasure);
    }


    public static Balance balance() throws IOException, InterruptedException, ExecutionException {
        return ActionRequest.balance();
    }


    public static Explore explore(ExploreRequest exploreRequest) throws IOException, InterruptedException {
        return ActionRequest.explore(exploreRequest);
    }


    public static Map<Integer, List<Point>> getExplore() {
        long time = System.currentTimeMillis();

        Point startPoint = getMaxTreasuresArea();
        startPoint = new Point(1, 1);

        Logger.log("Time after getting startPoint : " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();

        ActionMultiRequest<ExploreRequest, Explore> actionMultiRequest = new ActionMultiRequest<>(ExploreRequest.class, Explore.class);

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = new TreeMap<>();

        //Опрашиваем всю карту в заданных границах и получаем мапу
        for (int x = 0; x < Constant.areaSize && startPoint.getX()+x < Constant.mapSize; x++) {
            for (int y = 0; y < Constant.areaSize && startPoint.getY()+y < Constant.mapSize; y = y + Constant.threadsCountExplore) {
                List<Explore> treasures = actionMultiRequest.getTreasureMap(startPoint.getX()+x, startPoint.getY()+y);

                for (Explore treasure: treasures) {
                    //Если сокровища в точке есть
                    if (treasure != null && treasure.getAmount() != 0) {
                        int treasureCount = treasure.getAmount();

                        //обновляем список с координатами сокровищ
                        List<Point> pointList = null;
                        if (treasureMap.containsKey(treasureCount)) {
                            pointList = treasureMap.get(treasureCount);
                        } else {
                            pointList = new ArrayList<>();
                        }
                        pointList.add(new Point(treasure.getArea().getPosX(), treasure.getArea().getPosY(), 1, treasure.getAmount()));
                        treasureMap.put(treasureCount, pointList);
                    }
                }
            }
        }

        String strTreasuresCount = "";
        for (Integer k : treasureMap.keySet()) {
            strTreasuresCount += k + "(count=" + treasureMap.get(k).size() + "), ";
        }

        Logger.log("Time after get treasure map: " + (System.currentTimeMillis() - time));
        Logger.log("Treasures count: " + strTreasuresCount);

        return treasureMap;
    }


    /**
     * Возвращает начальную точку для квадрата с максимальным числом сокровищ
     */
    private static Point getMaxTreasuresArea() {
        Point res = new Point(1, 1);

        //формируем список квадратных областей
        List<ExploreRequest> requestList = new ArrayList<>();
        for (int x = 1; x < Constant.mapSize; x = x + Constant.areaSize) {
            for (int y = 1; y < Constant.mapSize; y = y + Constant.areaSize) {
                int sizeX = Constant.areaSize;
                int sizeY = Constant.areaSize;
                if (x + sizeX > Constant.mapSize) sizeX = Constant.mapSize - x;
                if (y + sizeY > Constant.mapSize) sizeY = Constant.mapSize - y;
                ExploreRequest exploreRequest = new ExploreRequest(x, y, sizeX, sizeY);
                requestList.add(exploreRequest);
            }
        }

        //отправляем запросы для определения числа сокровищ в области
        List<Explore> treasures = ActionRequest.getMaxTreasuresAreaRequest(requestList);
        int maxAmountTreasures = 0;
        for (Explore explore: treasures) {
            if (explore != null && explore.getAmount() > maxAmountTreasures) {
                maxAmountTreasures = explore.getAmount();
                res = new Point(explore.getArea().getPosX(), explore.getArea().getPosY());
            }
        }
        Logger.log("Start point for explore: " + res + "; maxAmountTreasures = " + maxAmountTreasures);

        return res;
    }


    public static List<License> getLicenses(Client client, int count) {
        ActionMultiRequest<Integer[], License> actionMultiRequest = new ActionMultiRequest<>(Integer[].class, License.class);
        List<Integer[]> requestList = new ArrayList<>();

        int paidLicenses = 0;
        //Запрашием count-лицензий
        for (int i = 0; i < count; i++) {
            //Запрос платной лицензии - если есть деньги
            if (paidLicenses < Constant.paidLicensesCount
                    && client != null
                    && client.getMoney() != null
                    && client.getMoney().size() > 0) {
                paidLicenses++;
                requestList.add(new Integer[]{client.getMoney().get(0)});
                client.getMoney().remove(0);
            }
            //Запрос бесплатной лицензии
            else {
                requestList.add(new Integer[]{});
            }

//            requestList.add(new Integer[]{});

        }

        return actionMultiRequest.getLicenses(requestList);
    }
}
