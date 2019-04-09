package com.aaronzadev.model.implementation;

import com.aaronzadev.model.dao.InProductsDAO;
import com.aaronzadev.model.pojo.InDetails;
import com.aaronzadev.model.pojo.InProduct;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InProductsDAOImp implements InProductsDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private int rowsAffected;

    public InProductsDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int GetLastInId() {

        int lastIdId = 1;

        try {

            cstmt = connection.prepareCall("{ call LastInId () }");
            rs = cstmt.executeQuery();

            if(rs.next()){

                lastIdId = rs.getInt(1)+1;

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return lastIdId;
    }

    @Override
    public List<InProduct> GetAll() {
        return null;
    }

    @Override
    public InProduct GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(InProduct o) throws SQLException {

        rowsAffected = 0;

        try{

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call AddIn (?,?) }");
            cstmt.setString(1, o.getProviderName());
            cstmt.setString(2, o.getInObs());
            rowsAffected = cstmt.executeUpdate();
            cstmt.close();

            cstmt = connection.prepareCall("{ call AddInDets (?,?,?) }");

            for (InDetails dets : o.getInDetails()) {
                cstmt.setInt(1, dets.getInId());
                cstmt.setInt(2, dets.getProductId());
                cstmt.setInt(3, dets.getQuantityIn());
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
    public int Update(InProduct o) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(InProduct o) {
        return 0;
    }
}
