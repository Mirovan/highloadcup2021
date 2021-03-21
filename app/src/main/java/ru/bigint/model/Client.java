package ru.bigint.model;

import ru.bigint.model.response.License;

import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
    private CopyOnWriteArrayList<LicenseWrapper> licenseWrapperList;
    private CopyOnWriteArrayList<Integer> money;

    public Client() {
    }

    public Client(CopyOnWriteArrayList<LicenseWrapper> licenseWrapperList, CopyOnWriteArrayList<Integer> money) {
        this.licenseWrapperList = licenseWrapperList;
        this.money = money;
    }

    public CopyOnWriteArrayList<LicenseWrapper> getLicenseWrapperList() {
        return licenseWrapperList;
    }

    public void setLicenseWrapperList(CopyOnWriteArrayList<LicenseWrapper> licenseWrapperList) {
        this.licenseWrapperList = licenseWrapperList;
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
                "licenseWrapperList=" + licenseWrapperList +
                ", money=" + money +
                '}';
    }
}
