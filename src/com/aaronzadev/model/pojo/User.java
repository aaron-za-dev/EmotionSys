package com.aaronzadev.model.pojo;

public class User{

    private short UserID;
    private String NickName;
    private String Password;
    private String FNames;
    private String LPName;
    private String LMName;
    private short Role;

    public User() {
    }

    public User(short userID, String nickName, String FNames, String LPName, String LMName, short role) {
        UserID = userID;
        NickName = nickName;
        this.FNames = FNames;
        this.LPName = LPName;
        this.LMName = LMName;
        Role = role;
    }

    public short getUserID() {
        return UserID;
    }

    public void setUserID(short userID) {
        UserID = userID;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFNames() {
        return FNames;
    }

    public void setFNames(String FNames) {
        this.FNames = FNames;
    }

    public String getLPName() {
        return LPName;
    }

    public void setLPName(String LPName) {
        this.LPName = LPName;
    }

    public String getLMName() {
        return LMName;
    }

    public void setLMName(String LMName) {
        this.LMName = LMName;
    }

    public short getRole() {
        return Role;
    }

    public void setRole(short role) {
        Role = role;
    }

    @Override
    public String toString() {
        return getNickName();
    }
    
    
    
    
    
}