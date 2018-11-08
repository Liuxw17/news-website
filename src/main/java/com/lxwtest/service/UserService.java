package com.lxwtest.service;

import com.lxwtest.dao.UserDAO;
import com.lxwtest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    public User getUser(int id){
        return userDAO.selectById(id);
    }

}
