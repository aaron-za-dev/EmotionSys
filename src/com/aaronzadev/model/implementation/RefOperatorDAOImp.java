package com.aaronzadev.model.implementation;

import com.aaronzadev.model.dao.RefOperatorDAO;
import com.aaronzadev.model.pojo.RefOperator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefOperatorDAOImp implements RefOperatorDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<RefOperator> ops;

    public RefOperatorDAOImp(Connection connection) {
        this.connection = connection;
    }   

    @Override
    public List<RefOperator> GetAll() {
        
        ops = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllRefOperators () }");

            rs = cstmt.executeQuery();

            while (rs.next()) {

                ops.add(new RefOperator(rs.getShort(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ops;

    }

    @Override
    public RefOperator GetById(int ID) {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Insert(RefOperator t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Update(RefOperator t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Delete(RefOperator t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
