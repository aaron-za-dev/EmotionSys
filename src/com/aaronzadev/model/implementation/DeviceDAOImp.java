package com.aaronzadev.model.implementation;



import com.aaronzadev.model.dao.DeviceDAO;
import com.aaronzadev.model.pojo.Device;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAOImp implements DeviceDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private List<Device> devices;
    private int rowsAffected;

    public DeviceDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Device> getAllByBrandAndType(short type, short brand) {

        devices = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetAllDevicesByBrandAndType (?,?) }");
            cstmt.setShort(1, type);
            cstmt.setShort(2, brand);

            rs = cstmt.executeQuery();

            while (rs.next()) {

                devices.add(new Device(rs.getInt(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return devices;
    }

    @Override
    public List<Device> GetAll() {
        return null;
    }

    @Override
    public Device GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(Device device) throws SQLException {
        return 0;
    }

    @Override
    public int Update(Device device) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(Device device) {
        return 0;
    }
}
