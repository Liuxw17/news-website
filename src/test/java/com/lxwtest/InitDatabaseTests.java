package com.lxwtest;

import com.lxwtest.NewsApplication;
import com.lxwtest.dao.LoginTicketDAO;
import com.lxwtest.dao.NewsDAO;
import com.lxwtest.dao.UserDAO;
import com.lxwtest.model.LoginTicket;
import com.lxwtest.model.News;
import com.lxwtest.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NewsApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests{
    @Autowired
    UserDAO userDAO;
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Test
    //测试往数据库写入数据
    public void contextLoads() {
        Random random = new Random();
        for (int i=0;i<11;i++){
            User user = new User();

            //增加用户资料
            user.setHeadUrl(String.format("http://images.xxx.com/head/%dt.png",random.nextInt(1000)));
            user.setName(String.format("USER%d",i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600*5*i);//以毫秒为单位计时
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.xxx.com/head/%dm.png",random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}",i));
            news.setLink(String.format("http://www.xxx.com/%d.html",i));
            newsDAO.addNews(news);

            //更新设置用户密码
            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            //验证ticket
            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
        }

        //用断言来测试功能
        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());//判断密码是否为newpassword
        userDAO.deleteById(1);//删除id=1的账户
        Assert.assertNull(userDAO.selectById(1));//判断id=1的账户是否为空

        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());

    }


}