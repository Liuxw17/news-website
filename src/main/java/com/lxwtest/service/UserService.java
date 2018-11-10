package com.lxwtest.service;

import com.lxwtest.dao.LoginTicketDAO;
import com.lxwtest.dao.UserDAO;
import com.lxwtest.model.LoginTicket;
import com.lxwtest.model.User;
import com.lxwtest.util.NewsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    //在UserService里增加获取用户信息的逻辑
    public User getUser(int id){
        return userDAO.selectById(id);
    }

    //在UserService里增加注册逻辑
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map = new HashMap<String, Object>();//Object代指很多
        if (StringUtils.isBlank(username)){//StringUtils.isBlank()常用在这里检测是否为空
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户已经被注册");
            return map;
        }

        user = new User();
        user .setName(username);
//        user.setPassword();//非常危险，不可取
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.xxx.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(NewsUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //注册的时候就加入ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //在UserService里增加登陆逻辑
    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<String, Object>();//Object代指很多
        if (StringUtils.isBlank(username)){//StringUtils.isBlank()常用在这里检测是否为空
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user ==null){
            map.put("msgname","用户不存在");
            return map;
        }

        if(!NewsUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }
        return map;
    }

    //设置ticket
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);//毫秒
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-",""));//把UUID里的-取消
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    //登出逻辑,直接让前端传来的ticket过期
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
