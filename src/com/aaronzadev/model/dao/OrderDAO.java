package com.aaronzadev.model.dao;

import com.aaronzadev.model.pojo.CustomerOrders;
import com.aaronzadev.model.pojo.Order;
import com.aaronzadev.model.pojo.OrderDetails;

import java.util.List;

public interface OrderDAO extends CRUD<Order> {

    int GetLastOrderID();

    CustomerOrders GetOrdersByCustomer(String phone);

    Order GetDetailsByOrderId(int orderId);

    int UpdateDetailsBySeller(List<OrderDetails> dets);

    int UpdateDetailsByTec(OrderDetails odets);

    List<OrderDetails> GetAllDetsByPriority(short priorityId);

    List<OrderDetails> GetAllDetsByService(short serviceId);

}
