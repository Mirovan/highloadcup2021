package ru.bigint;

import ru.bigint.model.ExploreRequest;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String address = System.getProperty("ADDRESS");
        String port = System.getProperty("Port");
        String schema = System.getProperty("Schema");
        String URI = schema + "://" + address + ":" + port;
        URI = "http://localhost:8080";

//        String get = Request.doGet(URI + "/balance");

        ExploreRequest exploreRequest = new ExploreRequest(1, 2, 3, 4);
        String post = Request.doPost(URI + "/explore", exploreRequest);

        System.out.println(post);

        //System.out.println("Hello");
    }
}