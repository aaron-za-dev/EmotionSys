package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.ProductCategoryDAO;
import com.aaronzadev.model.pojo.ProductCategory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDAOImp implements ProductCategoryDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<ProductCategory> cats;

    public ProductCategoryDAOImp(Connection connection) {
        this.connection = connection;
    }   

    @Override
    public List<ProductCategory> GetAll() {
        
        cats = new ArrayList<>();
        
        try {
            
            cstmt = connection.prepareCall("{ call GetAllProdCats () }");

            rs = cstmt.executeQuery();

            while (rs.next()){
                
                cats.add(new ProductCategory(rs.getShort(1), rs.getString(2), rs.getString(3)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }              
        
       return cats;
    }

    @Override
    public ProductCategory GetById(int ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Insert(ProductCategory t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Update(ProductCategory t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Delete(ProductCategory t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
