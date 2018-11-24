package com.lxwtest.controller;

import com.lxwtest.async.EventModel;
import com.lxwtest.async.EventProducer;
import com.lxwtest.async.EventType;
import com.lxwtest.service.NewsService;
import com.lxwtest.service.UserService;
import com.lxwtest.util.NewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    //注册
    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remberme, HttpServletResponse response) {

        try {
            Map<String, Object> map = userService.register(username, password);
            //Json串:{"code":0,"msg":"xxx"}
            if(map.containsKey("ticket")) {//包含ticket注册成功
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/"); //设置为全站有效
                if(remberme > 0){
                    cookie.setMaxAge(3600*24*5);//5天的秒数
                }
                response.addCookie(cookie);
                return NewsUtil.getJSONString(0, "注册成功");
            }else{
                return NewsUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("注册异常"+ e.getMessage());
            return NewsUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse response) {

        try {
            Map<String, Object> map = userService.login(username, password);
            //Json串:{"code":0,"msg":"xxx"}
            if(map.containsKey("ticket")) {//包含ticket注册成功
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (remember>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);//登录成功

                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                .setExt("username","本网站").setExt("to","admin@xxx.com"));

                return NewsUtil.getJSONString(0,"成功");
            }else{
                return NewsUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登录异常"+ e.getMessage());
            return NewsUtil.getJSONString(1,"登录异常");
        }
    }

    //登出
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody 需要去掉这个，否则return就当成字符串了
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);//登出
        return "redirect:/";//登出之后自动返回首页
    }
}