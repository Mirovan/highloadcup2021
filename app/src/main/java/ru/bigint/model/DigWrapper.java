package ru.bigint.model;

import ru.bigint.model.response.License;

import java.util.Arrays;

public class DigWrapper {
    private Point point;
    private License licence;
    private String[] response;

    @Override
    public String toString() {
        return "DigWrapper{" +
                "point=" + point +
                ", licence=" + licence +
                ", response=" + Arrays.toString(response) +
                '}';
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public License getLicence() {
        return licence;
    }

    public void setLicence(License licence) {
        this.licence = licence;
    }

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] response) {
        this.response = response;
    }

    public DigWrapper(Point point, License licence, String[] response) {
        this.point = point;
        this.licence = licence;
        this.response = response;
    }
}
