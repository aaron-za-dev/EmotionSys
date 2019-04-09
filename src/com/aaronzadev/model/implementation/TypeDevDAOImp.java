package com.aaronzadev.model.implementation;

import com.aaronzadev.model.dao.TypeDevDAO;
import com.aaronzadev.model.pojo.TypeDevice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeDevDAOImp implements TypeDevDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<TypeDevice> types;

    public TypeDevDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<TypeDevice> GetAll() {

        types = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllTypes () }");

            rs = cstmt.executeQuery();

            while (rs.next()) {

                types.add(new TypeDevice(rs.getShort(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }

    @Override
    public TypeDevice GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(TypeDevice typeDevice) throws SQLException {
        return 0;
    }

    @Override
    public int Update(TypeDevice typeDevice) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(TypeDevice typeDevice) {
        return 0;
    }
}
