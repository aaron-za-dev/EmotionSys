package com.aaronzadev.model.pojo;

public class SaleDetails {
    
    private int SaleID;
    private int ProductID;
    private short Quantity;
    private float PriceOut;

    private String prodName;
    private String barcode;

    public int getSaleID() {
        return SaleID;
    }

    public void setSaleID(int saleID) {
        SaleID = saleID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public short getQuantity() {
        return Quantity;
    }

    public void setQuantity(short quantity) {
        Quantity = quantity;
    }

    public float getPriceOut() {
        return PriceOut;
    }

    public void setPriceOut(float priceOut) {
        PriceOut = priceOut;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
