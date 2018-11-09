package com.lxwtest.controller;

import com.lxwtest.model.News;
import com.lxwtest.model.ViewObject;
import com.lxwtest.service.NewsService;
import com.lxwtest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model,@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int rember){

    try{
        Map<String,Object> map = userService.register(username,password);
        //Json串:{"code":0,"msg":"xxx"}

    }catch (Exception e)
    }
    return "";
}