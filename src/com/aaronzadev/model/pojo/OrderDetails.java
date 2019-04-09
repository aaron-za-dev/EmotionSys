package com.aaronzadev.model.pojo;

public class OrderDetails {

    private int orderId;
    private int deviceId;
    private short serviceId;
    private String noSerie;
    private String inObs;
    private short revStatus;
    private boolean fixStatus;
    private String fixObs;
    private boolean delivered;
    private float price;
    private short priorityService;
    private String dateDeliv;

    private String fullName;
    private String serviceName;
    //private String revStat;
    //private String fxiStat;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public short getServiceId() {
        return serviceId;
    }

    public void setServiceId(short serviceId) {
        this.serviceId = serviceId;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public String getInObs() {
        return inObs;
    }

    public void setInObs(String inObs) {
        this.inObs = inObs;
    }

    public short getRevStatus() {
        return revStatus;
    }

    public void setRevStatus(short revStatus) {
        this.revStatus = revStatus;
    }

    public boolean isFixStatus() {
        return fixStatus;
    }

    public void setFixStatus(boolean fixStatus) {
        this.fixStatus = fixStatus;
    }

    public String getFixObs() {
        return fixObs;
    }

    public void setFixObs(String fixObs) {
        this.fixObs = fixObs;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public short getPriorityService() {
        return priorityService;
    }

    public void setPriorityService(short priorityService) {
        this.priorityService = priorityService;
    }

    public String getDateDeliv() {
        return dateDeliv;
    }

    public void setDateDeliv(String dateDeliv) {
        this.dateDeliv = dateDeliv;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


}
