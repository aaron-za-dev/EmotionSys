package com.aaronzadev.model.pojo;

public class PriorityService {

    private final short IDPriority;
    private final String PriorityName;
    private final String EstimatedTime;

    public PriorityService(short IDPriority, String priorityName, String estimatedTime) {
        this.IDPriority = IDPriority;
        PriorityName = priorityName;
        EstimatedTime = estimatedTime;
    }

    public short getIDPriority() {
        return IDPriority;
    }

    @Override
    public String toString() {
        return PriorityName +" ("+ EstimatedTime+")";
    }
}
