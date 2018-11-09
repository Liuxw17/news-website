package com.lxwtest.service;

import com.lxwtest.dao.UserDAO;
import com.lxwtest.model.User;
import com.lxwtest.util.NewsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    //在UserService里增加获取用户信息的逻辑
    public User getUser(int id){
        return userDAO.selectById(id);
    }

    //在UserService里增加注册逻辑
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map = new HashMap<String, Object>();//Object代指很多
        if (StringUtils.isBlank(username)){//StringUtils.isBlank()常用在这里检测是否为空
            map.put("msgname","用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
        }

        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户已经被注册");
        }

        user = new User();
        user .setName(username);
//        user.setPassword();//非常危险，不可取
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.getHeadUrl(String.format("http://images.xxx.com/head/%dt.png",new Random().nextInt(1000));
        user.setPassword(NewsUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录

        return map;
    }
}
