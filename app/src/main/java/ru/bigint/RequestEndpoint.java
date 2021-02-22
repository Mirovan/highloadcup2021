package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RequestEndpoint {

    public static Explore explore(String uri, ExploreRequest exploreRequest) throws IOException, InterruptedException {
        Logger.log("-- Explore --");
        String body = Request.doPost(uri + "/explore", exploreRequest);
        MapperUtils<Explore> mapper = new MapperUtils<>(Explore.class);
        Explore explore = mapper.convertToObject(body);
        return explore;
    }

    public static License postLicense(String uri, int[] money) throws IOException, InterruptedException {
        Logger.log("-- Licence post --");
        String body = Request.doPost(uri + "/licenses", money);
        MapperUtils<License> mapper = new MapperUtils<>(License.class);
        License license = mapper.convertToObject(body);
//        License license = null;
//        try {
//            MapperUtils<License> exploreMapper = new MapperUtils<>(License.class);
//            license = exploreMapper.stringToObject(body);
//            Logger.log(license);
//        } catch (Exception e) {
//            Logger.log("JSON convert to Object error: " + e.getMessage());
//        }
        return license;
    }

    public static License license(String uri) throws IOException, InterruptedException {
        Logger.log("-- Licence post --");
        String body = Request.doGet(uri + "/licenses");
        MapperUtils<License> mapper = new MapperUtils<>(License.class);
        License license = mapper.convertToObject(body);

//        String body = Request.doGet(uri + "/licenses");
//        MapperUtils<License> exploreMapper = new MapperUtils<>(License.class);
//        License license = exploreMapper.stringToObject(body);
//        Logger.log(license);
        return license;
    }

    public static String[] dig(String uri, DigRequest digRequest) throws IOException, InterruptedException {
        Logger.log("-- Dig --");
        String body = Request.doPost(uri + "/dig", digRequest);
        MapperUtils<String[]> mapper = new MapperUtils<>(String[].class);
        String[] dig = mapper.convertToObject(body);

//        String body = Request.doPost(uri + "/dig", digRequest);
//        String[] dig = null;
//        try {
//            MapperUtils<String[]> exploreMapper = new MapperUtils<>(String[].class);
//            dig = exploreMapper.stringToObject(body);
//            Logger.log(Arrays.stream(dig).collect(Collectors.joining(", ")));
//        } catch (Exception e) {
//            Logger.log("JSON convert to Object error: " + e.getMessage());
//        }
        return dig;
    }

    public static int[] cash(String uri, String treasure) throws IOException, InterruptedException {
        Logger.log("-- Cash --");
        String body = Request.doPost(uri + "/cash", treasure);
        MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
        int[] money = mapper.convertToObject(body);

//        String body = Request.doPost(uri + "/cash", treasure);
//        MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
//        int[] money = mapper.stringToObject(body);
//        Logger.log( Arrays.stream(money).mapToObj(item -> ((Integer) item).toString()).collect(Collectors.joining(", ")) );
        return money;
    }

    public static String healthCheck(String uri) throws IOException, InterruptedException {
        String body = Request.doGet(uri + "/health-check");
//        MapperUtils<HealthCheck> mapper = new MapperUtils<>(HealthCheck.class);
//        HealthCheck healthCheck = mapper.stringToObject(body);
//        Logger.log(healthCheck);
//        return healthCheck;
        Logger.log(body);
        return body;
    }
}