package ru.bigint;

import ru.bigint.model.*;
import ru.bigint.model.response.Balance;
import ru.bigint.model.response.License;

import java.net.http.HttpClient;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Actions {

    private static ExecutorService threadPoolExplore = Executors.newFixedThreadPool(Constant.threadsCountExplore);
    //    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);
//    private static ExecutorService threadPoolLicense = Executors.newFixedThreadPool(Constant.threadsCountLicense);
    private static ExecutorService threadPoolCash = Executors.newFixedThreadPool(Constant.threadsCountCash);

    static final Object lockLicense = new Object();
    static final Object lockDig = new Object();

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

/*        synchronized (lockLicense) {
            //если есть деньги на платную лицензию
            if (client.getMoney().size() >= 0) {
                int coinIndex = client.getMoney().size()-1;
                requestObj = new Integer[]{client.getMoney().get(coinIndex)};
                client.getMoney().remove(coinIndex);

                //сколько монет заплатим за платную лицензию
//                requestObj = new Integer[Constant.paidForLicense];
//                for (int j = 0; j < Constant.paidForLicense; j++) {
//                    requestObj[j] = client.getMoney().get(0);
//                    client.getMoney().remove(0);
//                }
            }
        }*/

        //можем ли создать платную лицензию
        synchronized (lockLicense) {
            if (client.getMoney().size() > 0) {
                int coinIndex = client.getMoney().size()-1;
                requestObj = new Integer[]{client.getMoney().get(coinIndex)};
                client.getMoney().remove(coinIndex);
            }
        }

        License clientLicense = SimpleRequest.license(requestObj, client);

        synchronized (lockLicense) {
            if (clientLicense != null) {
                client.getLicenseWrapperList().add(new LicenseWrapper(clientLicense, 0));
            }
        }

        LoggerUtil.logFinishTime("Get/Update Licenses time:");
    }


    /**
     * Асинхронные раскопки
     */
    public static DigWrapper dig(CopyOnWriteArrayList<LicenseWrapper> licenseWrapperList, ConcurrentLinkedQueue<Point> digPointStack) {
        LoggerUtil.logStartTime();

        DigWrapper res = null;
        DigRequestWrapper digRequestWrapper = null;

        synchronized (lockDig) {
            //Выбираем лицензию для раскопок
            Optional<LicenseWrapper> licenseWrapperOpt = licenseWrapperList.stream()
                    .filter(item -> item.getUseCount() < item.getLicense().getDigAllowed())
                    .findFirst();

            if (licenseWrapperOpt.isPresent()) {
                LicenseWrapper licenseWrapper = licenseWrapperOpt.get();
                if (!digPointStack.isEmpty()) {
                    Point point = digPointStack.poll();
                    if (point != null) {
                        licenseWrapper.setUseCount(licenseWrapper.getUseCount() + 1);
                        digRequestWrapper = new DigRequestWrapper(point, licenseWrapper.getLicense());
                    }
                }
            }
        }

        if (digRequestWrapper != null && digRequestWrapper.getDigPoint() != null) {
            res = SimpleRequest.dig(digRequestWrapper);
        }

        LoggerUtil.logFinishTime("Dig time:");

        return res;
    }


    /**
     * Обмен сокровищ на деньги
     *
     * @param treasure
     * @return
     */
    public static CashWrapper cash(String treasure) {
        return SimpleRequest.cash(treasure);
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
