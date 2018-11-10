package com.lxwtest.interceptor;

import com.lxwtest.dao.LoginTicketDAO;
import com.lxwtest.dao.UserDAO;
import com.lxwtest.model.HostHolder;
import com.lxwtest.model.LoginTicket;
import com.lxwtest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//拦截器（需要把拦截器注册到MVC里面），可以做很多事
@Component
public class PassportInterceptor implements HandlerInterceptor {
//3个函数
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if(httpServletRequest.getCookies()!= null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket= cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){//查user是否有效
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }
            //确认用户是谁
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);//用户存在线程里面
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() !=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//收尾工作
        hostHolder.clear();
    }
}
