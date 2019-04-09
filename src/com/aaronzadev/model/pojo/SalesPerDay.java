/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaronzadev.model.pojo;

/**
 *
 * @author Aaron
 */
public class SalesPerDay {
    
    private final float Total;
    private final int Quantity;

    public SalesPerDay(float Total, int Quantity) {
        this.Total = Total;
        this.Quantity = Quantity;
    }    
    
    public int getQuantity() {
        return Quantity;
    }

    public float getTotal() {
        return Total;
    }   
    
}
