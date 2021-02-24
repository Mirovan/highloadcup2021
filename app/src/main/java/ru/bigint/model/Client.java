package ru.bigint.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client {
    private List<License> licenses;

    public Client() {
    }

    public Client(List<License> licenses) {
        this.licenses = licenses;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    @Override
    public String toString() {
        String licenses = this.licenses.stream()
                .map(License::toString)
                .collect(Collectors.joining(", "));
        return "Client{" +
                "license=" + licenses +
                '}';
    }
}
