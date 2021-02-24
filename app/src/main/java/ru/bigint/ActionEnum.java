package ru.bigint;

public enum ActionEnum {

    ALL("/"),
    HEALTH_CHECK("/health-check"),
    EXPLORE("/explore"),
    LICENSES("/licenses"),
    DIG("/dig"),
    CASH("/cash"),
    BALANCE("/balance");

    private String request;

    ActionEnum(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

}
