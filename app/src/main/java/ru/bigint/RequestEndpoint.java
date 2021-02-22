package ru.bigint;

import ru.bigint.model.DigRequest;
import ru.bigint.model.Explore;
import ru.bigint.model.ExploreRequest;
import ru.bigint.model.License;

import java.io.IOException;

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
        return license;
    }

    public static License license(String uri) throws IOException, InterruptedException {
        Logger.log("-- Licence post --");
        String body = Request.doGet(uri + "/licenses");
        MapperUtils<License> mapper = new MapperUtils<>(License.class);
        License license = mapper.convertToObject(body);
        return license;
    }

    public static String[] dig(String uri, DigRequest digRequest) throws IOException, InterruptedException {
        Logger.log("-- Dig --");
        String body = Request.doPost(uri + "/dig", digRequest);
        MapperUtils<String[]> mapper = new MapperUtils<>(String[].class);
        String[] dig = mapper.convertToObject(body);
        return dig;
    }

    public static int[] cash(String uri, String treasure) throws IOException, InterruptedException {
        Logger.log("-- Cash --");
        String body = Request.doPost(uri + "/cash", treasure);
        MapperUtils<int[]> mapper = new MapperUtils<>(int[].class);
        int[] money = mapper.convertToObject(body);
        return money;
    }

    public static String healthCheck(String uri) throws IOException, InterruptedException {
        String body = Request.doGet(uri + "/health-check");
        Logger.log(body);
        return body;
    }
}