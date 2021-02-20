package ru.bigint.emulator.model;

public class LicencesResponse {
    private int id;
    private int digAllowed;
    private int digUsed;

    public LicencesResponse() {
    }

    public LicencesResponse(int id, int digAllowed, int digUsed) {
        this.id = id;
        this.digAllowed = digAllowed;
        this.digUsed = digUsed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDigAllowed() {
        return digAllowed;
    }

    public void setDigAllowed(int digAllowed) {
        this.digAllowed = digAllowed;
    }

    public int getDigUsed() {
        return digUsed;
    }

    public void setDigUsed(int digUsed) {
        this.digUsed = digUsed;
    }
}
