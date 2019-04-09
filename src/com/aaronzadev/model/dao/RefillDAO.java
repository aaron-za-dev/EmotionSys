package com.aaronzadev.model.dao;

import com.aaronzadev.model.pojo.Refill;

import java.util.List;

public interface RefillDAO extends CRUD<Refill> {

    List<Refill> GetAllOfToday();

    List<Refill> GetAllOfWeek();

    List<Refill> GetAllOfMonth();

    List<Refill> GetAllByOperator(short OpId);

    List<Refill> GetAllByUser(short UserId);
    
}
