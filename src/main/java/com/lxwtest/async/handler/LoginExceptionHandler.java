package com.lxwtest.async.handler;

import com.lxwtest.async.EventHandler;
import com.lxwtest.async.EventModel;
import com.lxwtest.async.EventType;
import com.lxwtest.model.Message;
import com.lxwtest.service.MessageService;
import com.lxwtest.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
//实例化
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //判断是否有登录异常（此处省略）
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆IP异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        //发送Email
        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));//导入username
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆异常", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);//关注登录
    }
}
