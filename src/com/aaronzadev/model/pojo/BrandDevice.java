package com.aaronzadev.model.pojo;

public class BrandDevice {

    private final short BrandId;
    private final String BrandName;

    public BrandDevice(short brandId, String brandName) {
        BrandId = brandId;
        BrandName = brandName;
    }

    public short getBrandId() {
        return BrandId;
    }

    public String getBrandName() {
        return BrandName;
    }

    @Override
    public String toString() {
        return BrandId +" "+BrandName;
    }
}
