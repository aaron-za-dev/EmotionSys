/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.SalesPerDayDAO;
import com.aaronzadev.model.pojo.SalesPerDay;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesPerDayDAOImp implements SalesPerDayDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;

    public SalesPerDayDAOImp(Connection connection) {
        this.connection = connection;
    }  
    

    @Override
    public SalesPerDay GetSalesPerDay() {
        
        SalesPerDay sales = null;
        
        try {

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call GetTotalSalesByDay() }");

            rs = cstmt.executeQuery();

            while (rs.next()){
                
                sales = new SalesPerDay(rs.getFloat(1), rs.getInt(2));

            }

            connection.commit();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {

            try {
                cstmt.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        
        
        
        return sales;
    }    
}
