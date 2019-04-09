package com.aaronzadev.model.pojo;

public class TypeDevice {

    private final short TypeId;
    private final String TypeName;

    public TypeDevice(short typeId, String typeName) {
        TypeId = typeId;
        TypeName = typeName;
    }

    public short getTypeId() {
        return TypeId;
    }

    public String getTypeName() {
        return TypeName;
    }

    @Override
    public String toString() {
        return TypeId +" "+TypeName;
    }
}
