package ru.bigint.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Client {
    private List<License> licenses;
    private List<Integer> money;

    public Client() {
    }

    public Client(List<License> licenses, List<Integer> money) {
        this.licenses = licenses;
        this.money = money;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public List<Integer> getMoney() {
        return money;
    }

    public void setMoney(List<Integer> money) {
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
