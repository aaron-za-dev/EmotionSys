package com.aaronzadev.model.pojo;

public class FixStatus {

    private final short fixStatusId;
    private final String fixStatusName;

    public FixStatus(short fixStatusId, String fixStatusName) {
        this.fixStatusId = fixStatusId;
        this.fixStatusName = fixStatusName;
    }

    public short getFixStatusId() {
        return fixStatusId;
    }

    public String getFixStatusName() {
        return fixStatusName;
    }

    @Override
    public String toString() {
        return fixStatusId +" "+ fixStatusName;
    }
}
