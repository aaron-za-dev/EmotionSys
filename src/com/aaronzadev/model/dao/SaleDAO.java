package com.aaronzadev.model.dao;


import com.aaronzadev.model.pojo.FullSale;
import com.aaronzadev.model.pojo.Sale;

import java.util.List;

public interface SaleDAO extends CRUD<Sale> {
    
    int GetLastSaleID();

    List<String> GetAllOfToday();

    List<String> GetAllOfWeek();

    List<String> GetAllOfMonth();

    List<String> GetAllByDate(String month);

    List<String> GetAllByUser(short userId);

    FullSale GetFullSaleByID(int ID);
    
}
