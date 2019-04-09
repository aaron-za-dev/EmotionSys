package com.aaronzadev.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {

    List<T> GetAll();
    T GetById(int ID);
    int Insert(T o) throws SQLException;
    int Update(T o) throws SQLException;
    int Delete(T o);

}
