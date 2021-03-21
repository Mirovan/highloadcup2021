package ru.bigint.model;

import ru.bigint.model.response.License;

public class LicenseWrapper {
    private License license;
    private Integer useCount;

    public LicenseWrapper(License license, Integer useCount) {
        this.license = license;
        this.useCount = useCount;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    @Override
    public String toString() {
        return "LicenseWrapper{" +
                "license=" + license +
                ", useCount=" + useCount +
                '}';
    }
}
