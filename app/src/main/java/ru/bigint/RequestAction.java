package ru.bigint;

public enum RequestAction {

    ALL("/"),
    HEALTH_CHECK("/health-check"),
    EXPLORE("/explore"),
    LICENSES("/licenses"),
    DIG("/dig"),
    CASH("/cash");

    private String request;

    RequestAction(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

}
