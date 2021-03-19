package ru.bigint.model;

import ru.bigint.model.response.License;

import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
    private CopyOnWriteArrayList<License> licenses;
    private CopyOnWriteArrayList<Integer> money;

    public Client() {
    }

    public Client(CopyOnWriteArrayList<License> licenses, CopyOnWriteArrayList<Integer> money) {
        this.licenses = licenses;
        this.money = money;
    }

    public CopyOnWriteArrayList<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(CopyOnWriteArrayList<License> licenses) {
        this.licenses = licenses;
    }

    public CopyOnWriteArrayList<Integer> getMoney() {
        return money;
    }

    public void setMoney(CopyOnWriteArrayList<Integer> money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Client{" +
                "licenses=" + licenses +
                ", money=" + money +
                '}';
    }
}
