package ru.bigint;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String address = System.getProperty("ADDRESS");
        String port = System.getProperty("Port");
        String schema = System.getProperty("Schema");
        String URI = schema + "://" + address + ":" + port;
        URI = "http://localhost:8080";

        String get = Request.doGet(URI + "/balance");

        System.out.println(get);

        //System.out.println("Hello");
    }
}