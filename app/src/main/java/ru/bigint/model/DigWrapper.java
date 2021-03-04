package ru.bigint.model;

import ru.bigint.model.request.DigRequest;

import java.util.Arrays;

public class DigWrapper {
    private DigRequest digRequest;
    private String[] treasures;

    public DigWrapper(DigRequest digRequest, String[] treasures) {
        this.digRequest = digRequest;
        this.treasures = treasures;
    }

    public DigRequest getDigRequest() {
        return digRequest;
    }

    public void setDigRequest(DigRequest digRequest) {
        this.digRequest = digRequest;
    }

    public String[] getTreasures() {
        return treasures;
    }

    public void setTreasures(String[] treasures) {
        this.treasures = treasures;
    }

    @Override
    public String toString() {
        return "DigWrapper{" +
                "digRequest=" + digRequest +
                ", treasures=" + Arrays.toString(treasures) +
                '}';
    }
}