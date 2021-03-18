package ru.bigint;

import ru.bigint.model.*;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.response.License;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Actions {

    private static ExecutorService threadPoolExplore = Executors.newFixedThreadPool(Constant.threadsCountExplore);
    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);
    private static ExecutorService threadPoolLicense = Executors.newFixedThreadPool(Constant.threadsCountLicense);
    private static ExecutorService threadPoolCash = Executors.newFixedThreadPool(Constant.threadsCountCash);


    /**
     * Возвращает список точек с сокровищами
     */
    public static List<Point> getPoints(int line) {
        LoggerUtil.logStartTime();

        //Делим строку line на partSize-частей и для каждой этой части делаем бинарный поиск
        int partSize = 100;

        //Формирую список N-частей для запросов для всей карты
        List<CompletableFuture<List<Point>>> cfList = new ArrayList<>();
        for (int part = 0; part < Constant.mapSize; part += partSize) {
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            int left = part;
            int right = Math.min(part + partSize, Constant.mapSize);
            cf.completeAsync(() -> AlgoUtils.binSearch(line, left, right), threadPoolExplore);
            cfList.add(cf);
        }

        List<Point> res = cfList.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        LoggerUtil.logFinishTime("Get Points time:");
        return res;
    }


    /**
     * Асинхронные раскопки
     */
    public static List<DigWrapper> dig(List<License> licenses, ConcurrentLinkedQueue<Point> digPointStack) {
        LoggerUtil.logStartTime();

        //Формируем список с объектами-запросами
        List<DigRequest> requestList = new ArrayList<>();
        //Просматриваем все лицензии
        for (License license : licenses) {
            //определяем сколько можем сделать раскопок в рамках этой лицензии
            for (int i = license.getDigUsed(); i < license.getDigAllowed(); i++) {
                if (!digPointStack.isEmpty()) {
                    Point point = digPointStack.poll();
                    DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), point.getDepth()+1);
                    requestList.add(digRequest);
                }
            }
        }

        //список с асинхронными запросами
        List<CompletableFuture<DigWrapper>> listCf = new ArrayList<>();
        for (int i = 0; i < requestList.size(); i++) {
            DigRequest requestObj = requestList.get(i);

            CompletableFuture<DigWrapper> cf = new CompletableFuture<>();

            cf.completeAsync(() -> SimpleRequest.dig(requestObj, licenses), threadPoolDig);
            listCf.add(cf);
        }

        List<DigWrapper> res = listCf.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        LoggerUtil.logFinishTime("Dig time:");

        return res;
    }


    /**
     * Получение и обновление лицензий
     * */
    public static void updateLicenses(Client client) {
        LoggerUtil.logStartTime();

        //Убираем истекшие лицензии
        List<License> licencesUpdate = new ArrayList<>();
        for (License lic: client.getLicenses()) {
            if (lic.getDigUsed() < lic.getDigAllowed()) {
                licencesUpdate.add(lic);
            }
        }
        client.setLicenses(licencesUpdate);

        //список с асинхронными запросами
        List<CompletableFuture<License>> listCf = new ArrayList<>();
        for (int i = client.getLicenses().size(); i < Constant.maxLicencesCount; i++) {
            //Бесплатная лицензия
            Integer[] requestObj = new Integer[]{};

            if (client.getMoney().size() >= Constant.paidForLicense) {
                requestObj = new Integer[Constant.paidForLicense];
                for (int j = 0; j < Constant.paidForLicense; j++) {
                    requestObj[j] = client.getMoney().get(0);
                    client.getMoney().remove(0);
                }
            }

            CompletableFuture<License> cf = new CompletableFuture<>();

            Integer[] finalRequestObj = requestObj;
            cf.completeAsync(() -> SimpleRequest.license(finalRequestObj), threadPoolLicense);
            listCf.add(cf);
        }

        List<License> licensesNew = listCf.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        client.getLicenses().addAll(licensesNew);

        LoggerUtil.logFinishTime("Get/Update Licenses time:");
    }


    /**
     * Обмен сокровищ на деньги
     *
     * @param treasures
     * @return*/
    public static List<Integer> cash(List<String> treasures) {
        LoggerUtil.logStartTime();

        //список с асинхронными запросами
        List<CompletableFuture<CashWrapper>> listCf = new ArrayList<>();
        for (int i = 0; i < treasures.size(); i++) {
            String requestObj = treasures.get(i);

            CompletableFuture<CashWrapper> cf = new CompletableFuture<>();

            cf.completeAsync(() -> SimpleRequest.cash(requestObj), threadPoolCash);
            listCf.add(cf);
        }

        List<CashWrapper> res = listCf.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        //Убираем из коллекции сокровищ то сокровище которое обменяли на деньги
        for (CashWrapper cashWrapper: res) {
            //ToDo: переделать на HashMap
            if (treasures!= null
                    && cashWrapper != null
                    && cashWrapper.getResponse() != null
                    && treasures.contains(cashWrapper.getRequest())) treasures.remove(cashWrapper.getRequest());
        }

        LoggerUtil.logFinishTime("Cash time:");
        return res.stream()
                .filter(item -> item != null && item.getResponse() != null)
                .map(CashWrapper::getResponse)
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

}
