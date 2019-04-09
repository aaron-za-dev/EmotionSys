package com.aaronzadev.model.pojo;

import java.util.List;

public class Order {

    private int OrderId;
    private int CustomId;
    private int UserId;
    private String OrderDate;
    private List<OrderDetails > details;

    public Order() {

    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getCustomId() {
        return CustomId;
    }

    public void setCustomId(int customId) {
        CustomId = customId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public List<OrderDetails> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetails> details) {
        this.details = details;
    }
}
