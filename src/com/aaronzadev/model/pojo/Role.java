package com.aaronzadev.model.pojo;


public class Role {
    
    private final short RoleID;
    private final String RoleName;

    public Role(short RoleID, String RoleName) {
        this.RoleID = RoleID;
        this.RoleName = RoleName;
    }

    public short getRoleID() {
        return RoleID;
    }

    public String getRoleName() {
        return RoleName;
    }   

    @Override
    public String toString() {
        return getRoleName();
    }   
    
    
}
