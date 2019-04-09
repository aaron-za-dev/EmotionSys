package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.BrandDevDAO;
import com.aaronzadev.model.pojo.BrandDevice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDevDAOImp implements BrandDevDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<BrandDevice> brands;

    public BrandDevDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<BrandDevice> GetAll() {

        brands = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllBrands () }");

            rs = cstmt.executeQuery();

            while (rs.next()) {

                brands.add(new BrandDevice(rs.getShort(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brands;
    }

    @Override
    public BrandDevice GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(BrandDevice brandDevice) throws SQLException {
        return 0;
    }

    @Override
    public int Update(BrandDevice brandDevice) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(BrandDevice brandDevice) {
        return 0;
    }
}
