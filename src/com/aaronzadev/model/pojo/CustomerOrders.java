package com.aaronzadev.model.pojo;

import java.util.List;

public class CustomerOrders {

    private Customer customer;
    private List<String> orders;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
}
