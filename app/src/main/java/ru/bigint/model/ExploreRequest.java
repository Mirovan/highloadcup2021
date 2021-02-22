package ru.bigint.model;

public class ExploreRequest {
    private int posX;
    private int posY;
    private int sizeX;
    private int sizeY;

    public ExploreRequest() {
    }

    public ExploreRequest(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    @Override
    public String toString() {
        return "ExploreRequest{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                '}';
    }
}
