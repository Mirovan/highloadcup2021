package ru.bigint.model;

public class Point {
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
}
