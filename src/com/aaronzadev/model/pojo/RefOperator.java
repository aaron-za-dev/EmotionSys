package com.aaronzadev.model.pojo;


public class RefOperator {
    
    private final short OperatorID;
    private final String OperatorName;

    public RefOperator(short OperatorID, String OperatorName) {
        this.OperatorID = OperatorID;
        this.OperatorName = OperatorName;
    }

    public short getOperatorID() {
        return OperatorID;
    }

    public String getOperatorName() {
        return OperatorName;
    }  

    @Override
    public String toString() {
        return OperatorID + " " + OperatorName;
    }   
    
}
