package com.aaronzadev.model.dao;

import com.aaronzadev.model.pojo.InProduct;

public interface InProductsDAO extends CRUD<InProduct> {

    int GetLastInId();

}
