package ru.bigint.model;

import ru.bigint.SimpleRequest;
import ru.bigint.model.request.ExploreRequest;
import ru.bigint.model.response.Explore;

import java.util.ArrayList;
import java.util.List;

public class AlgoUtils {
    /**
     * Бинарный поиск сокровищ для столбца на x-координате
     * */
    public static List<Point> binSearch(int x, int left, int right) {
        List<Point> res = new ArrayList<>();
        int middleIndex = (left + right) / 2;

        Explore exploreLeft = SimpleRequest.explore(new ExploreRequest(x, left, 1, middleIndex-left+1));
//        Explore exploreLeft = SimpleRequest.explore(new ExploreRequest(left, x, middleIndex-left+1, 1));
        int leftCount = 0;
        if (exploreLeft != null) leftCount = exploreLeft.getAmount();
        Explore exploreRight = SimpleRequest.explore(new ExploreRequest(x, middleIndex+1, 1, Math.max(1, right-middleIndex-1)));
//        Explore exploreRight = SimpleRequest.explore(new ExploreRequest(middleIndex+1, x, Math.max(1, right-middleIndex-1), 1));
        int rightCount = 0;
        if (exploreRight != null) rightCount = exploreRight.getAmount();

        if (leftCount != 0) {
            if (left == middleIndex) {
                res.add(new Point(x, left, 0, leftCount));
            } else {
                res.addAll(binSearch(x, left, middleIndex));
            }
        }
        if (rightCount != 0) {
            if (right == middleIndex+1) {
                res.add(new Point(x, right, 0, rightCount));
            } else {
                res.addAll(binSearch(x, middleIndex+1, right));
            }
        }

        return res;
    }
}
