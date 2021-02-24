package ru.bigint;

import ru.bigint.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Action {

    public static License license(int[] arr) throws IOException, InterruptedException {
        return ActionRequest.license(arr);
    }


    public static String[] dig(License license, Point point, int depth) throws IOException, InterruptedException {
        DigRequest digRequest = new DigRequest(license.getId(), point.getX(), point.getY(), depth);
        String[] treasures = ActionRequest.dig(digRequest);
        return treasures;
    }


    public static int[] cash(String treasure) throws IOException, InterruptedException {
        return ActionRequest.cash(treasure);
    }


    public static Balance balance() throws IOException, InterruptedException {
        return ActionRequest.balance();
    }


    public static Explore explore(ExploreRequest exploreRequest) throws IOException, InterruptedException {
        return ActionRequest.explore(exploreRequest);
    }

    public static Map<Integer, List<Point>> getExplore() {
        return ActionMultiRequest.getTreasureMap();
    }

    public static List<License> getLicenses(int[] arr) {
        return ActionMultiRequest.getLicenses(arr);
    }
}
