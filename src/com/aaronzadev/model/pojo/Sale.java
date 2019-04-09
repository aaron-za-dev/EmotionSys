package com.aaronzadev.model.pojo;

import java.util.List;

public class Sale {
    
    private int SaleID;
    private int UserID;
    private String SaleDate;
    private float Tax;
    private float SubTotal;
    private float Total;
    private List<SaleDetails> dets;
    
    public int getSaleID() {
        return SaleID;
    }

    public void setSaleID(int SaleID) {
        this.SaleID = SaleID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String SaleDate) {
        this.SaleDate = SaleDate;
    }

    public float getTax() {
        return Tax;
    }

    public void setTax(float Tax) {
        this.Tax = Tax;
    }

    public float getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(float SubTotal) {
        this.SubTotal = SubTotal;
    }

    public float getTotal() {
        return Total;
    }

    public void setTotal(float Total) {
        this.Total = Total;
    }

    public List<SaleDetails> getDets() {
        return dets;
    }

    public void setDets(List<SaleDetails> dets) {
        this.dets = dets;
    }


}
