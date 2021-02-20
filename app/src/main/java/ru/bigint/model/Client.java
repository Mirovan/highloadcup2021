package ru.bigint.model;

public class Client {
    private License license;

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "Client{" +
                "license=" + license +
                '}';
    }
}
