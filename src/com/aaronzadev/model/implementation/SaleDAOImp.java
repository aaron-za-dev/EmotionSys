package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.SaleDAO;
import com.aaronzadev.model.pojo.FullSale;
import com.aaronzadev.model.pojo.Sale;
import com.aaronzadev.model.pojo.SaleDetails;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleDAOImp implements SaleDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private int rowsAffected;
    private List<String> salesId;

    public SaleDAOImp(Connection connection) {
        this.connection = connection;
    }    

    @Override
    public List<Sale> GetAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale GetById(int ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Insert(Sale sale) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call AddSale (?,?,?,?) }");
            cstmt.setInt(1, sale.getUserID());
            cstmt.setFloat(2, sale.getTax());
            cstmt.setFloat(3, sale.getSubTotal());
            cstmt.setFloat(4, sale.getTotal());
            rowsAffected = cstmt.executeUpdate();
            cstmt.close();            
            
            cstmt = connection.prepareCall("{ call AddSaleDets (?,?,?,?) }");

            for (SaleDetails dets : sale.getDets()) {
                cstmt.setInt(1, dets.getSaleID());
                cstmt.setInt(2, dets.getProductID());
                cstmt.setInt(3, dets.getQuantity());
                cstmt.setFloat(4, dets.getPriceOut());
                cstmt.addBatch();
            }
            
            rowsAffected = cstmt.executeBatch().length;
            
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
    public int Update(Sale t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Delete(Sale t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int GetLastSaleID() {
        
        int lastSaleId = 1;
        
        try {

            cstmt = connection.prepareCall("{ call LastSaleId () }");
            rs = cstmt.executeQuery();
            
            if(rs.next()){
                
               lastSaleId = rs.getInt(1)+1;
            
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        
        return lastSaleId;
        
    }

    @Override
    public List<String> GetAllOfToday() {

        salesId = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetSalesOfToday() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                salesId.add(String.format("%010d", rs.getInt(1)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return salesId;

    }

    @Override
    public List<String> GetAllOfWeek() {

        salesId = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllSalesOfWeek() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                salesId.add(String.format("%010d", rs.getInt(1)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return salesId;
    }

    @Override
    public List<String> GetAllOfMonth() {

        salesId = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllSalesOfMonth() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                salesId.add(String.format("%010d", rs.getInt(1)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return salesId;
    }

    @Override
    public List<String> GetAllByDate(String date) {

        salesId = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllSalesByDate(?) }");
            cstmt.setString(1, date);

            rs = cstmt.executeQuery();

            while (rs.next()){

                salesId.add(String.format("%010d", rs.getInt(1)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return salesId;

    }

    @Override
    public List<String> GetAllByUser(short userId) {

        salesId = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllSalesByUser(?) }");
            cstmt.setShort(1, userId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                salesId.add(String.format("%010d", rs.getInt(1)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return salesId;
    }

    @Override
    public FullSale GetFullSaleByID(int ID) {

        FullSale fs = null;
        List<SaleDetails> dets = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetFullSale(?) }");
            cstmt.setInt(1,ID);

            rs = cstmt.executeQuery();

            while (rs.next()){

                if (fs == null) {

                    fs = new FullSale();
                    fs.setUName(rs.getString(1));
                    fs.setSaleDate(rs.getString(2));
                    fs.setIVA(rs.getFloat(3));
                    fs.setSubTotal(rs.getFloat(4));
                    fs.setTotal(rs.getFloat(5));
                    fs.setDetails(dets);

                    SaleDetails sd = new SaleDetails();
                    sd.setBarcode(rs.getString(6));
                    sd.setProdName(rs.getString(7));
                    sd.setQuantity(rs.getShort(8));
                    sd.setPriceOut(rs.getFloat(9));

                    dets.add(sd);


                } else {

                    SaleDetails sd = new SaleDetails();
                    sd.setBarcode(rs.getString(6));
                    sd.setProdName(rs.getString(7));
                    sd.setQuantity(rs.getShort(8));
                    sd.setPriceOut(rs.getFloat(9));

                    dets.add(sd);

                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return fs;

    }


}
