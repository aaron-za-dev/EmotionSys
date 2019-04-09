package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.TypeServDAO;
import com.aaronzadev.model.pojo.TypeService;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeServDAOImp implements TypeServDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<TypeService> services;

    public TypeServDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<TypeService> GetAll() {

        services = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllServices () }");

            rs = cstmt.executeQuery();

            while (rs.next()) {

                services.add(new TypeService(rs.getShort(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    @Override
    public TypeService GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(TypeService typeService) throws SQLException {
        return 0;
    }

    @Override
    public int Update(TypeService typeService) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(TypeService typeService) {
        return 0;
    }
}
