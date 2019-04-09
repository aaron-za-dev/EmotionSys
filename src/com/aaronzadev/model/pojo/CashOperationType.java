package com.aaronzadev.model.pojo;

public class CashOperationType {

    private final short OpTypeId;
    private final String OpTypeName;
    private final String PrefixName;

    public CashOperationType(short opTypeId, String opTypeName, String prefixName) {
        OpTypeId = opTypeId;
        OpTypeName = opTypeName;
        PrefixName = prefixName;
    }

    public short getOpTypeId() {
        return OpTypeId;
    }

    public String getPrefixName() {
        return PrefixName;
    }

    @Override
    public String toString() {
        return OpTypeId +" "+ OpTypeName;
    }
}
