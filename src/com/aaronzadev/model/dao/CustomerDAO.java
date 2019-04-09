package com.aaronzadev.model.dao;



import com.aaronzadev.model.pojo.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CRUD<Customer> {

    Customer GetByPhone(String phone) throws SQLException;

    List<Customer> GetAllByLetter(String letter) throws SQLException;

}
