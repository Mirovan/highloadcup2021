package ru.bigint;

import ru.bigint.model.Client;
import ru.bigint.model.DigRequest;
import ru.bigint.model.License;
import ru.bigint.model.Point;

import java.io.IOException;

public class Actions {

    public static String[] dig(Client client, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(client.getLicense().getId(), point.getX(), point.getY(), depth);
        String[] treasures = RequestEndpoint.dig(digRequest);
        return treasures;
    }

    public static License getLicense(int[] arr) throws IOException, InterruptedException {
        return RequestEndpoint.postLicense(arr);
    }

    public static int[] cash(String treasure) throws IOException, InterruptedException {
        return RequestEndpoint.cash(treasure);
    }
}
