package ru.bigint;

import ru.bigint.model.*;
import ru.bigint.model.response.Balance;
import ru.bigint.model.response.License;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Actions {

    private static ExecutorService threadPoolExplore = Executors.newFixedThreadPool(Constant.threadsCountExplore);
    //    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);
//    private static ExecutorService threadPoolLicense = Executors.newFixedThreadPool(Constant.threadsCountLicense);
    private static ExecutorService threadPoolCash = Executors.newFixedThreadPool(Constant.threadsCountCash);

//    static final Object lock = new Object();

    private static HttpClient httpClient = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(2))
            .build();

    /**
     * Возвращает список точек с сокровищами
     */
    public static CopyOnWriteArraySet<Point> getPoints(int line) {
        LoggerUtil.logStartTime();

        //Формирую список N-частей для запросов для всей карты
        CopyOnWriteArraySet<CompletableFuture<List<Point>>> cfList = new CopyOnWriteArraySet<>();
        for (int part = 0; part < Constant.mapSize; part += Constant.explorePartSize) {
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            int left = part;
            int right = Math.min(part + Constant.explorePartSize - 1, Constant.mapSize);
            cf.completeAsync(() -> AlgoUtils.binSearch(line, left, right), threadPoolExplore);
            cfList.add(cf);
        }

        List<Point> res = cfList.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        LoggerUtil.logFinishTime("Get Points time:");
        return new CopyOnWriteArraySet(res);
    }


    /**
     * Получение и обновление лицензий
     */
    public static void updateLicenses(Client client) {
        LoggerUtil.logStartTime();

        Integer[] requestObj = new Integer[]{};

        License clientLicense = SimpleRequest.license(requestObj, client);
        client.getLicenses().add(clientLicense);

/*
        CompletableFuture<License> cf = null;

        //Деньги для бесплатной лицензия
        Integer[] requestObj = new Integer[]{};

        License license = new License();

        //Убираем истекшие лицензии
        client.getLicenses().removeIf(item -> item != null
                && item.getId() != null
                && item.getDigUsed() >= item.getDigAllowed());

        //Если число лицензий меньше лимита - т.е. можно получить лицензию
        if (client.getLicenses().size() < Constant.maxLicencesCount.intValue()) {
            //Добавляем в список пустую лицензию
            client.getLicenses().add(license);

            //можем ли создать платную лицензию
//                if (client.getMoney().size() >= Constant.paidForLicense) {
//                    //сколько монет заплатим за платную лицензию
//                    requestObj = new Integer[Constant.paidForLicense];
//                    for (int j = 0; j < Constant.paidForLicense; j++) {
//                        requestObj[j] = client.getMoney().get(0);
//                        client.getMoney().remove(0);
//                    }
//                }

            cf = new CompletableFuture<>();
        }

        if (cf != null) {
            Integer[] finalRequestObj = requestObj;
            cf.completeAsync(() -> SimpleRequest.license(finalRequestObj, client));
            try {
                License serverLicense = cf.get();
                if (serverLicense == null) {
                    client.getLicenses().remove(license);
                } else {
                    license = serverLicense;
                }
            } catch (InterruptedException | ExecutionException e) {
                LoggerUtil.log(e.getMessage());
            }
        }

 */

        LoggerUtil.logFinishTime("Get/Update Licenses time:");
    }


    /**
     * Асинхронные раскопки
     */
    public static DigWrapper dig(CopyOnWriteArrayList<License> licenses, ConcurrentLinkedQueue<Point> digPointStack) {
        LoggerUtil.logStartTime();

        DigWrapper res = null;
        if (licenses.size() > 0) {
            License license = licenses.get(0);
            if (license.getDigUsed() < license.getDigAllowed()) {
                Point point = digPointStack.poll();

                DigRequestWrapper digRequestWrapper = new DigRequestWrapper(point, license);

                res = SimpleRequest.dig(digRequestWrapper, licenses);
            }
        }
        LoggerUtil.logFinishTime("Dig time:");

        return res;
    }


    /**
     * Обмен сокровищ на деньги
     *
     * @param treasures
     * @return
     */
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
//        for (CashWrapper cashWrapper : res) {
//            //ToDo: переделать на HashMap
//            if (treasures != null
//                    && cashWrapper != null
//                    && cashWrapper.getResponse() != null
//                    && treasures.contains(cashWrapper.getRequest())) treasures.remove(cashWrapper.getRequest());
//        }

        LoggerUtil.logFinishTime("Cash time:");
        return res.stream()
                .filter(item -> item != null && item.getResponse() != null)
                .map(CashWrapper::getResponse)
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }


    public static Balance balance() {
        return SimpleRequest.balance();
    }


//    public static void cashWithoutResponse(List<String> treasures) {
//        for (int i = 0; i < treasures.size(); i++) {
//            String requestObj = treasures.get(i);
//
//            CompletableFuture.runAsync(
//                    () -> SimpleRequest.cash(requestObj),
//                    threadPoolCash
//            );
//        }
//    }

}
