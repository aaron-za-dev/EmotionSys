package com.aaronzadev.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MExecutor {
    
    private static Executor exec;
    
    private MExecutor(){
       
        /*exec = Executors.newFixedThreadPool(5, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);        
                t.setDaemon(true);
                return t;
            }
        });*/
        
        exec = Executors.newCachedThreadPool( r-> {        
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
            
    }
    
    public static Executor getExecutor(){
    
        if(exec == null){
            
            new MExecutor();
            
        }
        
        return exec;        
    } 
    
}
