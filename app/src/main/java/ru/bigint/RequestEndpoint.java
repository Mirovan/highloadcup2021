package ru.bigint;

import ru.bigint.model.DigRequest;
import ru.bigint.model.ExploreRequest;
import ru.bigint.model.Explore;
import ru.bigint.model.License;

import java.io.IOException;

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
        Logger.log(dig);
        return dig;
    }
}
