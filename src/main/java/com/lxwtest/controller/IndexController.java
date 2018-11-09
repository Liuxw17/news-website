package com.lxwtest.controller;

import com.lxwtest.model.User;
import com.lxwtest.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//本页为测试，所以注释掉Controller
//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TestService testService;

    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit Index");
        return "Hello!"+ session.getAttribute("msg")
                +"<br>Say:"+ testService.say();
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue = "lxwtest") String key ){//注意对应类型
        return  String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }

    @RequestMapping(value = {"/vm"})//测试templates
    public String news(Model model){
        model.addAttribute("value1","value1-test");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","YELLOW","BLACK"});//测试传值
        Map<String,String> map = new HashMap<String, String>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }

        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("lxw"));
        return "news";
    }

    /**
     * 测试request
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String>  headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        sb.append("<br>");
        for(Cookie cookie:request.getCookies()){
            sb.append("Cookie");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        sb.append("<br>");
        sb.append("getMethod:"+ request.getMethod()+"<br>");
        sb.append("getPathInfo:"+ request.getPathInfo()+"<br>");
        sb.append("getQueryString:"+ request.getQueryString()+"<br>");
        sb.append("getRequestURI:"+ request.getRequestURI()+"<br>");

        return sb.toString();
    }

    /**
     * 测试response
     * @param nowcoderId
     * @param key
     * @param value
     * @param response
     * @return
     */
    @RequestMapping(value = {"/response"})
    @ResponseBody
    public  String response(@CookieValue(value = "nowcoderId",defaultValue = "a")String nowcoderId,
                            @RequestParam(value = "key",defaultValue = "key")String key,
                            @RequestParam(value = "value",defaultValue = "value") String value,
                            HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowCoderId From Cookie:" + nowcoderId;
    }

    /**
     * 测试跳转
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("/redirect/{code}")
            //第一种跳转
//    public RedirectView redirect(@PathVariable("code") int code){
//        RedirectView red = new RedirectView("/",true);
//        if (code ==301){
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
//        return red;
    //第二种跳转
    public String redirect(@PathVariable("code") int code,
                                 HttpSession session){
        session.setAttribute("msg","Jump from redirect.");
        return "redirect:/";
    }


    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false) String key){
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }

    /**
     * 测试Error,自己定义一个ExceptionHandler
     * @param e
     * @return 返回一个统一的错误处理界面
     */
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error" + e.getMessage();
    }

}
