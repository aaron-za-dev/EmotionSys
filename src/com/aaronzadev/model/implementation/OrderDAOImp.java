package com.aaronzadev.model.implementation;


import com.aaronzadev.model.dao.OrderDAO;
import com.aaronzadev.model.pojo.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImp implements OrderDAO {

    private final Connection connection;
    private ResultSet rs;
    private CallableStatement cstmt;
    private int rowsAffected;
    private List<OrderDetails> details;

    public OrderDAOImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int GetLastOrderID() {

        int lastOrderId = 1;

        try {

            cstmt = connection.prepareCall("{ call LastOrderId () }");
            rs = cstmt.executeQuery();

            if(rs.next()){

                lastOrderId = rs.getInt(1)+1;

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return lastOrderId;

    }

    @Override
    public CustomerOrders GetOrdersByCustomer(String phone) {

        CustomerOrders cusorders = null;
        List<String> orders = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetOrdersByCustomer (?) }");
            cstmt.setString(1, phone);

            rs = cstmt.executeQuery();

            while (rs.next()){

                if(cusorders == null){

                    Customer c = new Customer();
                    c.setCustFName(rs.getString(1));
                    c.setCustLPName(rs.getString(2));
                    c.setCustLMName(rs.getString(3));
                    c.setStreet(rs.getString(4));
                    c.setNum(rs.getInt(5));
                    c.setColony(rs.getString(6));
                    c.setSPhonNum(rs.getString(7));

                    cusorders = new CustomerOrders();
                    cusorders.setCustomer(c);

                    orders.add(String.format("%010d", rs.getInt(8)));
                    cusorders.setOrders(orders);

                }
                else {

                    orders.add(String.format("%010d", rs.getInt(8)));

                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return cusorders;

    }

    @Override
    public List<Order> GetAll() {
        return null;
    }

    @Override
    public Order GetById(int ID) {
        return null;
    }

    @Override
    public int Insert(Order order) throws SQLException {

        rowsAffected = 0;

        try{

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{ call AddOrder (?,?) }");
            cstmt.setInt(1, order.getCustomId());
            cstmt.setInt(2, order.getUserId());
            rowsAffected = cstmt.executeUpdate();
            cstmt.close();

            cstmt = connection.prepareCall("{ call AddOrderDets (?,?,?,?,?,?,?) }");

            for (OrderDetails details : order.getDetails()) {
                cstmt.setInt(1, details.getOrderId());
                cstmt.setInt(2, details.getDeviceId());
                cstmt.setShort(3, details.getServiceId());
                cstmt.setString(4, details.getNoSerie());
                cstmt.setString(5, details.getInObs());
                cstmt.setShort(6, details.getPriorityService());
                cstmt.setFloat(7, details.getPrice());
                cstmt.addBatch();
            }

            rowsAffected = cstmt.executeBatch().length;

            connection.commit();

        }catch(SQLException e){
            connection.rollback();
            throw e;
        }

        return rowsAffected;

    }

    @Override
    public Order GetDetailsByOrderId(int orderId) {

        Order order = null;
        List<OrderDetails> dets = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetOrderDetsByOrderId (?) }");
            cstmt.setInt(1, orderId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                if(order == null){

                    order = new Order();
                    order.setOrderId(rs.getInt(1));
                    order.setOrderDate(rs.getString(2));

                    OrderDetails od = new OrderDetails();
                    od.setOrderId(rs.getInt(1));
                    od.setDeviceId(rs.getInt(3));
                    od.setFullName(rs.getString(4));
                    od.setNoSerie(rs.getString(5));
                    od.setInObs(rs.getString(6));
                    od.setRevStatus(rs.getShort(7));
                    od.setFixStatus(rs.getBoolean(8));
                    od.setFixObs(rs.getString(9));
                    od.setDelivered(rs.getBoolean(10));
                    od.setDateDeliv(rs.getString(11));
                    od.setServiceName(rs.getString(12));
                    od.setPriorityService(rs.getShort(13));
                    od.setPrice(rs.getFloat(14));

                    dets.add(od);
                    order.setDetails(dets);

                }
                else {

                    OrderDetails od = new OrderDetails();
                    od.setOrderId(rs.getInt(1));
                    od.setDeviceId(rs.getInt(3));
                    od.setFullName(rs.getString(4));
                    od.setNoSerie(rs.getString(5));
                    od.setInObs(rs.getString(6));
                    od.setRevStatus(rs.getShort(7));
                    od.setFixStatus(rs.getBoolean(8));
                    od.setFixObs(rs.getString(9));
                    od.setDelivered(rs.getBoolean(10));
                    od.setDateDeliv(rs.getString(11));
                    od.setServiceName(rs.getString(12));
                    od.setPriorityService(rs.getShort(13));
                    od.setPrice(rs.getFloat(14));
                    dets.add(od);

                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return order;

    }

    @Override
    public int UpdateDetailsBySeller(List<OrderDetails> odets) {

        rowsAffected = 0;

        try {

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{call UpdateOrderDetailsBySeller(?,?)}");

            for (OrderDetails det : odets){

                cstmt.setInt(1, det.getOrderId());
                cstmt.setInt(2, det.getDeviceId());
                //cstmt.setBoolean(3, det.isDelivered());
                cstmt.addBatch();
            }

            rowsAffected = cstmt.executeBatch().length;

            connection.commit();

        }catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return rowsAffected;
    }

    @Override
    public int UpdateDetailsByTec(OrderDetails odets) {

        rowsAffected = 0;

        try {

            connection.setAutoCommit(false);

            cstmt = connection.prepareCall("{call UpdateOrderDetailsByTec(?,?,?,?,?)}");
            cstmt.setInt(1, odets.getOrderId());
            cstmt.setInt(2, odets.getDeviceId());
            cstmt.setShort(3, odets.getRevStatus());
            cstmt.setBoolean(4, odets.isFixStatus());
            cstmt.setString(5, odets.getFixObs());

            rowsAffected = cstmt.executeUpdate();

            connection.commit();

        }catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return rowsAffected;
    }

    @Override
    public List<OrderDetails> GetAllDetsByPriority(short priorityId) {

        details = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetOrderDetsByPriority (?) }");
            cstmt.setShort(1, priorityId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                OrderDetails order = new OrderDetails();
                order.setOrderId(rs.getInt(1));
                order.setDeviceId(rs.getInt(2));
                order.setFullName(rs.getString(3));
                order.setNoSerie(rs.getString(4));
                order.setInObs(rs.getString(5));
                order.setRevStatus(rs.getShort(6));
                order.setFixStatus(rs.getBoolean(7));
                order.setFixObs(rs.getString(8));
                order.setServiceName(rs.getString(9));
                order.setPriorityService(rs.getShort(10));
                order.setPrice(rs.getFloat(11));

                details.add(order);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return details;
    }

    @Override
    public List<OrderDetails> GetAllDetsByService(short serviceId) {

        details = new ArrayList<>();

        try {

            cstmt = connection.prepareCall("{ call GetOrderDetsByService (?) }");
            cstmt.setShort(1, serviceId);

            rs = cstmt.executeQuery();

            while (rs.next()){

                OrderDetails order = new OrderDetails();
                order.setOrderId(rs.getInt(1));
                order.setDeviceId(rs.getInt(2));
                order.setFullName(rs.getString(3));
                order.setNoSerie(rs.getString(4));
                order.setInObs(rs.getString(5));
                order.setRevStatus(rs.getShort(6));
                order.setFixStatus(rs.getBoolean(7));
                order.setFixObs(rs.getString(8));
                order.setServiceName(rs.getString(9));
                order.setPriorityService(rs.getShort(10));
                order.setPrice(rs.getFloat(11));

                details.add(order);

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return details;
    }

    @Override
    public int Update(Order order) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(Order order) {
        return 0;
    }
}
