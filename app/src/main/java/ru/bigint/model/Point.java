package ru.bigint.model;

public class Point {
    private int x;
    private int y;
    private int depth;
    private int treasureDepth;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int depth, int treasureDepth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.treasureDepth = treasureDepth;
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

    public int getTreasureDepth() {
        return treasureDepth;
    }

    public void setTreasureDepth(int treasureDepth) {
        this.treasureDepth = treasureDepth;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", depth=" + depth +
                ", treasureDepth=" + treasureDepth +
                '}';
    }
}
