package ru.bigint.model;

import java.util.Objects;

public class Point implements Comparable<Point> {
    private int x;
    private int y;
    private int depth;
    private int treasuresCount;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int depth, int treasuresCount) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.treasuresCount = treasuresCount;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getTreasuresCount() {
        return treasuresCount;
    }

    public void setTreasuresCount(int treasuresCount) {
        this.treasuresCount = treasuresCount;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", depth=" + depth +
                ", treasuresCount=" + treasuresCount +
                '}';
    }

    @Override
    public int compareTo(Point point) {
        if (this.getX() != point.getX()) {
            return Integer.compare(this.getX(), point.getX());
        } else {
            return Integer.compare(this.getY(), point.getY());
        }
    }
}
