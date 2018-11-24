package com.lxwtest;

import com.lxwtest.model.User;
import com.lxwtest.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NewsWebsiteApplication.class)
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testJedis() {
        jedisAdapter.set("hello", "world");
        Assert.assertEquals("world", jedisAdapter.get("hello"));
    }
    @Test
    public void testObject() {
        User user = new User();
        user.setHeadUrl("http://xxx.com/head/100t.png");
        user.setName("user1");
        user.setPassword("abc");
        user.setSalt("def");
        jedisAdapter.setObject("user1xxx", user);

        User u = jedisAdapter.getObject("user1xxx", User.class);
        System.out.print(ToStringBuilder.reflectionToString(u));

    }

}