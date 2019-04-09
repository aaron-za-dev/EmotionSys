package com.aaronzadev.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    
    private final String LOCAL_URL = "jdbc:mysql://localhost:3306/emotioncelulardb?useSSL=true&serverTimezone=UTC";
    private final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
   
    private final String ADMIN_USER = "root";
    private final String ADMIN_PASS = "root";

    private static DbConnection instance;
    private static ComboPooledDataSource ds;
    private static Connection connection;
    
    public DbConnection (){
        
        try {

            ds = new ComboPooledDataSource();
            ds.setDriverClass(DRIVER_CLASS);
            ds.setJdbcUrl(LOCAL_URL);
            ds.setUser(ADMIN_USER);
            ds.setPassword(ADMIN_PASS);            
            ds.setInitialPoolSize(0);
            ds.setMinPoolSize(0);
            ds.setAcquireIncrement(1);
            ds.setMaxPoolSize(5);
            ds.setMaxStatements(180);
            ds.setAcquireRetryAttempts(2);
            ds.setBreakAfterAcquireFailure(true);
            
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    
    public static Connection getConnection() {

        if (instance == null) {

            instance = new DbConnection();

            try {

                connection = ds.getConnection();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }
    
}
