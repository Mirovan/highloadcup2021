package ru.bigint.model;

import java.util.Arrays;

public class CashWrapper {
    private String request;
    private Integer[] response;

    public CashWrapper(String request, Integer[] response) {
        this.request = request;
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer[] getResponse() {
        return response;
    }

    public void setResponse(Integer[] response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "CashWrapper{" +
                "request='" + request + '\'' +
                ", response=" + Arrays.toString(response) +
                '}';
    }
}
