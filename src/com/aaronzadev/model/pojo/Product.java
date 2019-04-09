package com.aaronzadev.model.pojo;

public class Product {
    
    private int ProductID;
    private short CatID;
    private String BarCode;
    private String Name;
    private String Desc;
    private float Price;
    private short Stock;   
    
    public Product(int ProductID, short CatID, String BarCode, String Name, String Desc, float Price, short Stock) {
        this.ProductID = ProductID;
        this.CatID = CatID;
        this.BarCode = BarCode;
        this.Name = Name;
        this.Desc = Desc;
        this.Price = Price;
        this.Stock = Stock;
    }

    public Product() {
    }   

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public short getCatID() {
        return CatID;
    }

    public void setCatID(short catID) {
        CatID = catID;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String BarCode) {
        this.BarCode = BarCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }

    public short getStock() {
        return Stock;
    }

    public void setStock(short Stock) {
        this.Stock = Stock;
    }

    @Override
    public String toString() {
        return Name+" - Codigo: "+BarCode;
    }
}
