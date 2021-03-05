package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.response.License;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
//        Logger.log("--- Running App ---");

        long startTime = System.currentTimeMillis();

        Main main = new Main();
        main.runGame();

//        Logger.log("Time: " + (System.currentTimeMillis() - startTime));
    }

    private void runGame() throws IOException, InterruptedException {
//        Logger.log("--- Play Game ---");
//        Logger.log("OS: " + System.getProperty("os.name"));
//        Logger.log("URI: " + URI);

//        RequestEndpoint.healthCheck(URI);

        Client client = new Client();
        client.setLicenses(new ArrayList<>());

        //коллекция для хранения сокровищ. ключ - число сокровищ, значения - список координат
        Map<Integer, List<Point>> treasureMap = Action.getExploreMap();

        List<Integer> treasureAmountList = new ArrayList<>(treasureMap.keySet());

        //ToDo: for log
        String strTres = "";
        for (Integer k : treasureMap.keySet()) {
            strTres += k + "=>" + treasureMap.get(k).size() + "; ";
        }
        Logger.log(strTres);

        //Формируем стек из ячеек
        Stack<Point> stack = new Stack<>();
        for (Integer pointTreasureCount : treasureAmountList) {
            List<Point> points = treasureMap.get(pointTreasureCount);
            if (points != null) {
                stack.addAll(points);
            }
        }


        //Достаем по несколько элементов и асинхронно отправляем запрос
        while ( !stack.isEmpty() ) {
//            List<License> licenses = new ArrayList<>();
            //Запрос - сколько у нас лицензий
//            License[] licensesArr = ActionRequest.license();
//            int licenseCount = 0;
//            if (licensesArr != null) {
//                licenseCount = licensesArr.length;
//                licenses.addAll(Arrays.asList(licensesArr));
//            }
            //Делаем запросы на получение новы лицензий
//            licenses.addAll(Action.getLicenses(client, Constant.threadsCountLicenses - licenseCount));

            {
                List<License> licenses = new ArrayList<>();
                //Удаляем истекшие лицензии
                for (License license : client.getLicenses()) {
                    if (license.getDigUsed() >= license.getDigAllowed()) licenses.add(license);
                }
                List<License> newLicenses = Action.getLicenses(client, Constant.threadsCountLicenses - licenses.size());
                licenses.addAll(newLicenses);
                client.setLicenses(licenses);
                Logger.log(ActionEnum.LICENSES, "Before dig NEW Licenses: " + newLicenses.stream().map(Objects::toString).collect(Collectors.joining("; ")));
                Logger.log(ActionEnum.LICENSES, "Before dig: " + client.getLicenses().stream().map(Objects::toString).collect(Collectors.joining("; ")));
            }

            //Список точек из стека
            List<Point> digPoints = new ArrayList<>();
            for (int i = 0; i < client.getLicenses().size(); i++) {
                if (!stack.isEmpty()) {
                    digPoints.add(stack.pop());
                }
            }

            //делаем асинхронные запросы на раскопки
            List<DigWrapper> digs = ActionRequest.dig(digPoints, client.getLicenses());

//            Logger.log("Dig size: " + digs.size());

            //Просматриваем результаты раскопок
            for (DigWrapper dig : digs) {
//                Logger.log("Dig item: " + dig);
                if (dig != null) {

                    Point point = null;
                    if (dig.getDigRequest() != null) {
                        DigRequest digRequest = dig.getDigRequest();
                        //находим это точку в списке
                        for (Point p : digPoints) {
                            if (p.getX() == digRequest.getPosX() && p.getY() == digRequest.getPosY()) {
                                point = p;
                            }
                        }
                    }

                    //Если найдено сокровище - обновляем данные у точки
                    if (dig.getTreasures() != null) {
                        //обновляем лицензию
                        int idLicense = dig.getDigRequest().getLicenseID();
                        for (License license: client.getLicenses()) {
                            if (license.getId() == idLicense) {
                                license.setDigUsed(license.getDigUsed() + 1);
                            }
                        }

                        //Обновляем число найденных сокровищ для точки
                        point.setTreasuresCount(point.getTreasuresCount() - dig.getTreasures().length);

                        //Обмениваем сокровища на золото
                        for (String treasure : dig.getTreasures()) {
                            if (client.getMoney() == null) client.setMoney(new LinkedList<>());
                            //### CASH ###
                            Integer[] money = Action.cash(treasure);
                            if (money == null) money = new Integer[0];
                            client.getMoney().addAll(Arrays.asList(money));
                        }
                    }

                    //Если у точки еще есть сокровища, то возвращаем точку в стек
                    if (point.getTreasuresCount() > 0) {
                        //Если раскопки удались - то увеличиваем глубину
                        if (dig.getTreasures() != null) {
                            point.setDepth(point.getDepth() + 1);
                        }
                        stack.add(point);
                    }
                }
            }

            Logger.log(ActionEnum.LICENSES, "After dig: " + client.getLicenses().stream().map(Objects::toString).collect(Collectors.joining("; ")));

        }
    }

}