package com.aaronzadev.model.pojo;

public class Device {

    private int DeviceId;
    private String DeviceModel;
    private short BrandId;
    private short TypeId;

    public Device(int deviceId, String deviceModel) {
        DeviceId = deviceId;
        DeviceModel = deviceModel;
    }

    public Device() {

    }

    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }

    public short getBrandId() {
        return BrandId;
    }

    public void setBrandId(short brandId) {
        BrandId = brandId;
    }

    public short getTypeId() {
        return TypeId;
    }

    public void setTypeId(short typeId) {
        TypeId = typeId;
    }

    @Override
    public String toString() {
        return  DeviceModel;
    }
}
