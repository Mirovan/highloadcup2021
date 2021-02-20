package ru.bigint.emulator.model;

public class HealthCheckResponse {
    private String additionalProp1;

    public HealthCheckResponse() {
    }

    public HealthCheckResponse(String additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    public Object getAdditionalProp1() {
        return additionalProp1;
    }

    public void setAdditionalProp1(String additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }
}
