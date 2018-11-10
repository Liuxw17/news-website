package com.lxwtest.configuration;

import com.lxwtest.interceptor.LoginRequiredInterceptor;
import com.lxwtest.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//拦截器自动配置,注册拦截器
//定义Component初始化
@Component
public class NewsWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override //添加拦截器，Ctrl+O 快捷键调出
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);//注册，先看看拦截的用户是谁
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");//注册，再看看用户是否符合要求，这里设置只管理setting页面
        super.addInterceptors(registry);
    }
}
