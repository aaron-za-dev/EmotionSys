package com.aaronzadev.model.pojo;


public class Refill {
    
    private int RefillID;
    private short UserID;
    private short OperatorID;
    private float Amount;
    private float Commission;
    private String PhoneNum;
    private String Reference;
    private String Obs;
    private String RefillDate;

    private String Nickname;
    private String OperatorName;
    
    public int getRefillID() {
        return RefillID;
    }

    public void setRefillID(int RefillID) {
        this.RefillID = RefillID;
    }

    public short getUserID() {
        return UserID;
    }

    public void setUserID(short UserID) {
        this.UserID = UserID;
    }

    public short getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(short OperatorID) {
        this.OperatorID = OperatorID;
    }   

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float Amount) {
        this.Amount = Amount;
    }

    public float getCommission() {
        return Commission;
    }

    public void setCommission(float Commission) {
        this.Commission = Commission;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String PhoneNum) {
        this.PhoneNum = PhoneNum;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String Reference) {
        this.Reference = Reference;
    }

    public String getObs() {
        return Obs;
    }

    public void setObs(String Obs) {
        this.Obs = Obs;
    }

    public String getRefillDate() {
        return RefillDate;
    }

    public void setRefillDate(String RefillDate) {
        this.RefillDate = RefillDate;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }
}
