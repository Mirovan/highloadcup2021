package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;

public class Actions {

    public static License license(int[] arr) throws IOException, InterruptedException {
        return RequestEndpoint.postLicense(arr);
    }


    public static String[] dig(Client client, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(client.getLicense().getId(), point.getX(), point.getY(), depth);
        String[] treasures = RequestEndpoint.dig(digRequest);
        return treasures;
    }


    public static int[] cash(String treasure) throws IOException, InterruptedException {
        return RequestEndpoint.cash(treasure);
    }


    public static Balance balance() throws IOException, InterruptedException {
        return RequestEndpoint.balance();
    }

}
