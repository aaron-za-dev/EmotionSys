package com.aaronzadev.model.pojo;

public class CashOperation {

    private int OperationId;
    private short TypeOperation;
    private short UserId;
    private String Reference;
    private float Amount;
    private String DateOp;
    private String ObsOp;

    //TODO add additional fields like username and operation name

    public int getOperationId() {
        return OperationId;
    }

    public void setOperationId(int operationId) {
        OperationId = operationId;
    }

    public short getTypeOperation() {
        return TypeOperation;
    }

    public void setTypeOperation(short typeOperation) {
        TypeOperation = typeOperation;
    }

    public short getUserId() {
        return UserId;
    }

    public void setUserId(short userId) {
        UserId = userId;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getDateOp() {
        return DateOp;
    }

    public void setDateOp(String dateOp) {
        DateOp = dateOp;
    }

    public String getObsOp() {
        return ObsOp;
    }

    public void setObsOp(String obsOp) {
        ObsOp = obsOp;
    }
}
