package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.CustomerDAO;
import com.aaronzadev.model.pojo.Customer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImp implements CustomerDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<Customer> customs;
    private int rowsAffected;

    public CustomerDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> GetAll() {
        return null;
    }

    @Override
    public Customer GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(Customer c) throws SQLException {

        rowsAffected = 0;

        try{

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call AddCustomer (?,?,?,?,?,?,?,?) }");
            cstmt.setString(1, c.getCustFName());
            cstmt.setString(2, c.getCustLPName());
            cstmt.setString(3, c.getCustLMName());
            cstmt.setString(4, c.getStreet());
            cstmt.setInt(5, c.getNum());
            cstmt.setString(6, c.getColony());
            cstmt.setString(7, c.getFPhonNum());
            cstmt.setString(8, c.getSPhonNum());

            rowsAffected = cstmt.executeUpdate();

            connection.commit();

        }catch(SQLException e){
           connection.rollback();
           throw e;
        }/*finally {

            cstmt.close(); TODO forget free all statments and resultsets


        }*/

        return rowsAffected;
    }

    @Override
    public int Update(Customer c) throws SQLException {

        rowsAffected = 0;

        try{

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call UpdateCustomer (?,?,?,?,?,?,?,?,?) }");
            cstmt.setInt(1, c.getCustId());
            cstmt.setString(2, c.getCustFName());
            cstmt.setString(3, c.getCustLPName());
            cstmt.setString(4, c.getCustLMName());
            cstmt.setString(5, c.getStreet());
            cstmt.setInt(6, c.getNum());
            cstmt.setString(7, c.getColony());
            cstmt.setString(8, c.getFPhonNum());
            cstmt.setString(9, c.getSPhonNum());

            rowsAffected = cstmt.executeUpdate();

            connection.commit();

        }catch(SQLException e){
            connection.rollback();
            throw e;
        }

        return rowsAffected;
    }

    @Override
    public int Delete(Customer customer) {
        return 0;
    }

    @Override
    public Customer GetByPhone(String phone) throws SQLException {

        Customer c = null;

        try{

            cstmt = connection.prepareCall("{ call GetCustomerByPhoneNum (?) }");
            cstmt.setString(1, phone);

            rs = cstmt.executeQuery();

            while (rs.next()){

                c = new Customer(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9)
                );

            }

        }catch(SQLException e){
            throw e;
        }

        return c;
    }

    @Override
    public List<Customer> GetAllByLetter(String letter) throws SQLException {

        customs = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllCustomerByLetter(?) }");
            cstmt.setString(1, letter);

            rs = cstmt.executeQuery();

            while (rs.next()){

                customs.add(new Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                ));

            }

        }
        catch (SQLException e){
            throw e;
        }

        return customs;
    }
}
