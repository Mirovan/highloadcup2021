package ru.bigint.model;

import ru.bigint.model.response.License;

public class DigRequestWrapper {
    private Point digPoint;
    private License license;

    public DigRequestWrapper(Point digPoint, License license) {
        this.digPoint = digPoint;
        this.license = license;
    }

    public Point getDigPoint() {
        return digPoint;
    }

    public void setDigPoint(Point digPoint) {
        this.digPoint = digPoint;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "DigRequestWrapper{" +
                "digPoint=" + digPoint +
                ", license=" + license +
                '}';
    }
}
