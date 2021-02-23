package ru.bigint;

public enum RequestEnum {

    ALL("/"),
    HEALTH_CHECK("/health-check"),
    EXPLORE("/explore"),
    LICENSES("/licenses"),
    DIG("/dig"),
    CASH("/cash");

    private String request;

    RequestEnum(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

}
