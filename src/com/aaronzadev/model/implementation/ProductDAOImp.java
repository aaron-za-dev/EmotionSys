package com.aaronzadev.model.implementation;

import com.aaronzadev.model.dao.ProductDAO;
import com.aaronzadev.model.pojo.Product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImp implements ProductDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private int rowsAffected;

    public ProductDAOImp(Connection connection) {
        this.connection = connection;
    }
    

    @Override
    public Product GetProductByBarCode(String barcode) {

        Product product = null;

        try {

            cstmt = connection.prepareCall("{ call GetProductByBarcode (?) }");
            cstmt.setString(1, barcode);

            rs = cstmt.executeQuery();

            while (rs.next()) {

                product = new Product(
                        rs.getInt(1),
                        rs.getShort(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getFloat(6),
                        rs.getShort(7)
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    @Override
    public List<Product> GetAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product GetById(int ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Insert(Product t) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call AddProduct (?,?,?,?,?,?) }");
            cstmt.setInt(1, t.getCatID());
            cstmt.setString(2, t.getBarCode());
            cstmt.setString(3, t.getName());
            cstmt.setString(4, t.getDesc());
            cstmt.setFloat(5, t.getPrice());
            cstmt.setShort(6, t.getStock());
            
            rowsAffected = cstmt.executeUpdate();
            
            connection.commit();
        
        }catch(SQLException e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        
        return rowsAffected;
        
    }

    @Override
    public int Update(Product t) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call UpdateProduct (?,?,?,?,?,?,?)}");
            cstmt.setInt(1, t.getProductID());
            cstmt.setShort(2, t.getCatID());
            cstmt.setString(3, t.getBarCode());
            cstmt.setString(4, t.getName());
            cstmt.setString(5, t.getDesc());
            cstmt.setFloat(6, t.getPrice());
            cstmt.setShort(7, t.getStock());
            
            rowsAffected = cstmt.executeUpdate();
            
            connection.commit();
        
        }catch(SQLException e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        
        return rowsAffected;
    
    }

    @Override
    public int Delete(Product t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int GetLastProductID() {
        
        int lastID = 1;
        
        try {

            cstmt = connection.prepareCall("{ call LastProdId () }");
            rs = cstmt.executeQuery();
            
            if(rs.next()){
                
               lastID = rs.getInt(1)+1;
            
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        
        return lastID;
    }

    @Override
    public List<Product> GetAllByCategoryID(short categoryId) {

        List<Product> prods = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllProdsByCategory (?) }");
            cstmt.setShort(1, categoryId);

            rs = cstmt.executeQuery();

            while (rs.next()) {

                prods.add(new Product(
                        rs.getInt(1),
                        rs.getShort(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getFloat(6),
                        rs.getShort(7)
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return prods;
    }
}
