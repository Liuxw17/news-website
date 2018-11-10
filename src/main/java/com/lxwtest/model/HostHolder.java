package com.lxwtest.model;

import org.springframework.stereotype.Component;

//表示当前的用户是谁
@Component
public class HostHolder {//用来存用户
    private static ThreadLocal<User> users = new ThreadLocal<>();//ThreadLocal线程本地变量

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
