package com.aaronzadev.model.dao;

import com.aaronzadev.model.pojo.User;

public interface UserDAO extends CRUD<User> {

    User getByNickAndPass(String nick, String pass);
    
    int UpdateUserAndPass(User u);

}
