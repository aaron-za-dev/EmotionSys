package com.aaronzadev.model.pojo;


public class ProductCategory {
    
    private final short CategoryID;
    private final String CategoryName;
    private final String PrefixCat;

    public ProductCategory(short CategoryID, String CategoryName, String PrefixCat) {
        this.CategoryID = CategoryID;
        this.CategoryName = CategoryName;
        this.PrefixCat = PrefixCat;
    }

    public short getCategoryID() {
        return CategoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getPrefixCat() {
        return PrefixCat;
    }   

    @Override
    public String toString() {
        return CategoryID+" "+CategoryName;
    }
    
    
    
}
