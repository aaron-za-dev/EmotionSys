package com.aaronzadev.model.pojo;

public class TypeService {

    private final short ServiceId;
    private final String ServiceName;

    public TypeService(short serviceId, String serviceName) {
        ServiceId = serviceId;
        ServiceName = serviceName;
    }

    public short getServiceId() {
        return ServiceId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    @Override
    public String toString() {
        return ServiceId + " " + ServiceName;
    }
}
