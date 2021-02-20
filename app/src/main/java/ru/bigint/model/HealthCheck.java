package ru.bigint.model;

public class HealthCheck {
    private Object additionalProp1;

    public HealthCheck() {
    }

    public HealthCheck(Object additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    public Object getAdditionalProp1() {
        return additionalProp1;
    }

    public void setAdditionalProp1(Object additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @Override
    public String toString() {
        return "HealthCheck{" +
                "additionalProp1=" + additionalProp1 +
                '}';
    }
}
