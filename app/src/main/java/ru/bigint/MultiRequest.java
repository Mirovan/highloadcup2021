package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.AlgoUtils;
import ru.bigint.model.DigWrapper;
import ru.bigint.model.Point;
import ru.bigint.model.request.DigRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;
import ru.bigint.model.response.License;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MultiRequest {

    private static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    private static ExecutorService threadPoolExplore = Executors.newFixedThreadPool(Constant.threadsCountExplore);
    private static ExecutorService threadPoolDig = Executors.newFixedThreadPool(Constant.threadsCountDig);


    /**
     * Возвращает список точек с сокровищами
     */
    public static List<Point> getPoints() {
        //Формирую список N-запросов для всей карты
        List<CompletableFuture<List<Point>>> cfList = new ArrayList<>();
        for (int x = 0; x < Constant.maxExploreX; x++) {
            int finalX = x;
            CompletableFuture<List<Point>> cf = new CompletableFuture<>();
            cf.completeAsync(() -> AlgoUtils.binSearch(finalX, 1, Constant.mapSize), threadPoolExplore);
            cfList.add(cf);
        }

        List<Point> res = cfList.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return res;
    }


    /**
     * Асинхронные раскопки
     */
    public static List<DigWrapper> dig(List<License> licenses, Stack<Point> digPointStack) {
        //Формируем список с объектами-запросами
        List<DigRequest> requestList = new ArrayList<>();
        for (License license : licenses) {
            for (int i = license.getDigUsed(); i < license.getDigAllowed(); i++) {
                if (!digPointStack.isEmpty()) {
                    Point point = digPointStack.pop();
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

        return res;
    }
}
