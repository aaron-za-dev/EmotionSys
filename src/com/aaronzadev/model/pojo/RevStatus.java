package com.aaronzadev.model.pojo;

public class RevStatus {

    private final short revId;
    private final String revName;

    public RevStatus(short revId, String revName) {
        this.revId = revId;
        this.revName = revName;
    }

    public short getRevId() {
        return revId;
    }

    public String getRevName() {
        return revName;
    }

    @Override
    public String toString() {
        return revId+" "+revName;
    }
}
