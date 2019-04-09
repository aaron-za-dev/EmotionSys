package com.aaronzadev.model.pojo;

import java.util.List;

public class InProduct {

    private int inId;
    private String inDate;
    private String providerName;
    private String inObs;
    private List<InDetails> inDetails;

    public int getInId() {
        return inId;
    }

    public void setInId(int inId) {
        this.inId = inId;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getInObs() {
        return inObs;
    }

    public void setInObs(String inObs) {
        this.inObs = inObs;
    }

    public List<InDetails> getInDetails() {
        return inDetails;
    }

    public void setInDetails(List<InDetails> inDetails) {
        this.inDetails = inDetails;
    }
}
