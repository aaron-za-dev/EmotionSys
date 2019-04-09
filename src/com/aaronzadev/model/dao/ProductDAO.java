package com.aaronzadev.model.dao;

import com.aaronzadev.model.pojo.Product;

import java.util.List;

public interface ProductDAO extends CRUD<Product> {
    
    Product GetProductByBarCode(String barcode);
    
    int GetLastProductID();

    List<Product> GetAllByCategoryID(short categoryId);
    
}
