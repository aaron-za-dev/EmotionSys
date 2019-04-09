package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.RefillDAO;
import com.aaronzadev.model.pojo.Refill;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefillDAOImp implements RefillDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<Refill> refills;
    private int rowsAffected;

    public RefillDAOImp(Connection connection) {
        this.connection = connection;
    }   

    @Override
    public List<Refill> GetAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Refill GetById(int ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Insert(Refill t) {
       
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call AddRefill (?,?,?,?,?,?,?) }");
                cstmt.setShort(1, t.getUserID());
            cstmt.setShort(2, t.getOperatorID());
            cstmt.setFloat(3, t.getAmount());
            cstmt.setFloat(4, t.getCommission());
            cstmt.setString(5, t.getPhoneNum());
            cstmt.setString(6, t.getReference());
            cstmt.setString(7, t.getObs());
            
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
    public int Update(Refill t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int Delete(Refill t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Refill> GetAllOfToday() {

        refills = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetRefillsOfToday() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                Refill ref = new Refill();
                ref.setNickname(rs.getString(1));
                ref.setOperatorName(rs.getString(2));
                ref.setAmount(rs.getFloat(3));
                ref.setCommission(rs.getFloat(4));
                ref.setPhoneNum(rs.getString(5));
                ref.setReference(rs.getString(6));
                ref.setObs(rs.getString(7));
                ref.setRefillDate(rs.getString(8));

                refills.add(ref);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return refills;

    }

    @Override
    public List<Refill> GetAllOfWeek() {

        refills = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllRefillsOfWeek() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                Refill ref = new Refill();
                ref.setNickname(rs.getString(1));
                ref.setOperatorName(rs.getString(2));
                ref.setAmount(rs.getFloat(3));
                ref.setCommission(rs.getFloat(4));
                ref.setPhoneNum(rs.getString(5));
                ref.setReference(rs.getString(6));
                ref.setObs(rs.getString(7));
                ref.setRefillDate(rs.getString(8));

                refills.add(ref);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return refills;
    }

    @Override
    public List<Refill> GetAllOfMonth() {

        refills = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllRefillsOfMonth() }");

            rs = cstmt.executeQuery();

            while (rs.next()){

                Refill ref = new Refill();
                ref.setNickname(rs.getString(1));
                ref.setOperatorName(rs.getString(2));
                ref.setAmount(rs.getFloat(3));
                ref.setCommission(rs.getFloat(4));
                ref.setPhoneNum(rs.getString(5));
                ref.setReference(rs.getString(6));
                ref.setObs(rs.getString(7));
                ref.setRefillDate(rs.getString(8));

                refills.add(ref);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return refills;
    }

    @Override
    public List<Refill> GetAllByOperator(short OpId) {

        refills = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllRefillsByOp(?) }");
            cstmt.setShort(1, OpId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                Refill ref = new Refill();
                ref.setNickname(rs.getString(1));
                ref.setOperatorName(rs.getString(2));
                ref.setAmount(rs.getFloat(3));
                ref.setCommission(rs.getFloat(4));
                ref.setPhoneNum(rs.getString(5));
                ref.setReference(rs.getString(6));
                ref.setObs(rs.getString(7));
                ref.setRefillDate(rs.getString(8));

                refills.add(ref);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return refills;

    }

    @Override
    public List<Refill> GetAllByUser(short UserId) {

        refills = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllRefillsByUser(?) }");
            cstmt.setShort(1, UserId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                Refill ref = new Refill();
                ref.setNickname(rs.getString(1));
                ref.setOperatorName(rs.getString(2));
                ref.setAmount(rs.getFloat(3));
                ref.setCommission(rs.getFloat(4));
                ref.setPhoneNum(rs.getString(5));
                ref.setReference(rs.getString(6));
                ref.setObs(rs.getString(7));
                ref.setRefillDate(rs.getString(8));

                refills.add(ref);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return refills;

    }
}
