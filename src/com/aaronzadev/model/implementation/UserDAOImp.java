package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.UserDAO;
import com.aaronzadev.model.pojo.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImp implements UserDAO {
    
    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<User> users;
    private int rowsAffected;

    public UserDAOImp(Connection connection) {
        this.connection = connection;
    }            

    @Override
    public List<User> GetAll() {
        
        users = new ArrayList<>();
        
        try {
            
            cstmt = connection.prepareCall("{ call GetAllUsers() }");

            rs = cstmt.executeQuery();

            while (rs.next()){
                
                users.add(new User(
                        rs.getShort(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getShort(6)));

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }              
        
       return users;
        
    }

    @Override
    public User GetById(int ID) {
        return null;
    }   

    @Override
    public int Insert(User t) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call AddUser (?,?,?,?,?,?) }");
            cstmt.setString(1, t.getNickName());
            cstmt.setString(2, t.getPassword());
            cstmt.setString(3, t.getFNames());
            cstmt.setString(4, t.getLPName());
            cstmt.setString(5, t.getLMName());
            cstmt.setInt(6, t.getRole());
            
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
    public int Update(User t) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call UpdateUser (?,?,?,?,?)}");
            cstmt.setInt(1, t.getUserID());
            cstmt.setString(2, t.getFNames());
            cstmt.setString(3, t.getLPName());
            cstmt.setString(4, t.getLMName());
            cstmt.setInt(5, t.getRole());
            
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
    public int UpdateUserAndPass(User t) {
        
        rowsAffected = 0;
        
        try{
            
            connection.setAutoCommit(false);
            
            cstmt = connection.prepareCall("{ call UpdateUserAndPass (?,?,?,?,?,?)}");
            cstmt.setInt(1, t.getUserID());
            cstmt.setString(2, t.getPassword());
            cstmt.setString(3, t.getFNames());
            cstmt.setString(4, t.getLPName());
            cstmt.setString(5, t.getLMName());
            cstmt.setInt(6, t.getRole());
            
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
    public int Delete(User t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    @Override
    public User getByNickAndPass(String nick, String pass) {
       
        User user = null;

        try {

            cstmt = connection.prepareCall("{ call GetUserByNickAndPass (?,?) }");
            cstmt.setString(1, nick);
            cstmt.setString(2, pass);

            rs = cstmt.executeQuery();

            while (rs.next()){

                user = new User(
                        rs.getShort(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getShort(6)
                );

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

      return user;

    }

    
    
}
