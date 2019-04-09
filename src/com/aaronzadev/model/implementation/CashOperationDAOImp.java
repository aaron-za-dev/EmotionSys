package com.aaronzadev.model.implementation;

import com.aaronzadev.model.dao.CashOperationDAO;
import com.aaronzadev.model.pojo.CashOperation;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CashOperationDAOImp implements CashOperationDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    //private List<Customer> customs;
    private int rowsAffected;

    public CashOperationDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CashOperation> GetAll() {
        return null;
    }

    @Override
    public CashOperation GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(CashOperation o) throws SQLException {

        rowsAffected = 0;

        try{

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call AddCashOperation (?,?,?,?,?) }");
            cstmt.setShort(1, o.getTypeOperation());
            cstmt.setShort(2, o.getUserId());
            cstmt.setString(3, o.getReference());
            cstmt.setFloat(4, o.getAmount());
            cstmt.setString(5, o.getObsOp());

            rowsAffected = cstmt.executeUpdate();

            connection.commit();

        }catch(SQLException e){
            connection.rollback();
            throw e;
        }

        return rowsAffected;

    }

    @Override
    public int Update(CashOperation o) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(CashOperation o) {
        return 0;
    }
}
