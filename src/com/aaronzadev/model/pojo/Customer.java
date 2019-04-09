package com.aaronzadev.model.pojo;

public class Customer{

    private int CustId;
    private String CustFName;
    private String CustLPName;
    private String CustLMName;
    private String Street;
    private int Num;
    private String Colony;
    private String FPhonNum;
    private String SPhonNum;

    public Customer() {

    }

    public Customer(int custId, String custFName, String custLPName, String custLMName, String street, int num, String colony, String FPhonNum, String SPhonNum) {
        CustId = custId;
        CustFName = custFName;
        CustLPName = custLPName;
        CustLMName = custLMName;
        Street = street;
        Num = num;
        Colony = colony;
        this.FPhonNum = FPhonNum;
        this.SPhonNum = SPhonNum;
    }

    public int getCustId() {
        return CustId;
    }

    public void setCustId(int custId) {
        CustId = custId;
    }

    public String getCustFName() {
        return CustFName;
    }

    public void setCustFName(String custFName) {
        CustFName = custFName;
    }

    public String getCustLPName() {
        return CustLPName;
    }

    public void setCustLPName(String custLPName) {
        CustLPName = custLPName;
    }

    public String getCustLMName() {
        return CustLMName;
    }

    public void setCustLMName(String custLMName) {
        CustLMName = custLMName;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public String getColony() {
        return Colony;
    }

    public void setColony(String colony) {
        Colony = colony;
    }

    public String getFPhonNum() {
        return FPhonNum;
    }

    public void setFPhonNum(String FPhonNum) {
        this.FPhonNum = FPhonNum;
    }

    public String getSPhonNum() {
        return SPhonNum;
    }

    public void setSPhonNum(String SPhonNum) {
        this.SPhonNum = SPhonNum;
    }

    @Override
    public String toString() {
        return CustFName +" "+CustLPName + " "+CustLMName+" * "+FPhonNum;
    }
}