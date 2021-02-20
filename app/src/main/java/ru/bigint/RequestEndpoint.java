package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RequestEndpoint {

    public static Explore explore(String uri, ExploreRequest exploreRequest) throws IOException, InterruptedException {
        String body = Request.doPost(uri + "/explore", exploreRequest);
        MapperUtils<Explore> exploreMapper = new MapperUtils<>(Explore.class);
        Explore explore = exploreMapper.stringToObject(body);
        Logger.log(explore);
        return explore;
    }

    public static License postLicense(String uri, int[] money) throws IOException, InterruptedException {
        String body = Request.doPost(uri + "/licences", money);
        MapperUtils<License> exploreMapper = new MapperUtils<>(License.class);
        License license = exploreMapper.stringToObject(body);
        Logger.log(license);
        return license;
    }

    public static License license(String uri) throws IOException, InterruptedException {
        String body = Request.doGet(uri + "/licences");
        MapperUtils<License> exploreMapper = new MapperUtils<>(License.class);
        License license = exploreMapper.stringToObject(body);
        Logger.log(license);
        return license;
    }

    public static String[] dig(String uri, DigRequest digRequest) throws IOException, InterruptedException {
        String body = Request.doPost(uri + "/dig", digRequest);
        MapperUtils<String[]> exploreMapper = new MapperUtils<>(String[].class);
        String[] dig = exploreMapper.stringToObject(body);
        Logger.log( Arrays.stream(dig).collect(Collectors.joining(", ")) );
        return dig;
    }

    public static int[] cash(String uri, String treasure) throws IOException, InterruptedException {
        String body = Request.doPost(uri + "/cash", treasure);
        MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
        int[] money = mapper.stringToObject(body);
        Logger.log( Arrays.stream(money).mapToObj(item -> ((Integer) item).toString()).collect(Collectors.joining(", ")) );
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