package com.aaronzadev.util;

import com.aaronzadev.model.pojo.User;

public class SessionManager {
    
    private User currentUser;
    private static /*volatile*/ SessionManager instance;

    private SessionManager(User user) {
        currentUser = user;
    }
    
    public static /*synchronized */SessionManager getCurrentSession (User u){
        
        if (instance == null) {            
            instance = new SessionManager(u);                    
        }
        
        return instance;       
    } 

    public User getCurrentUser() {
        return currentUser;
    }
}
