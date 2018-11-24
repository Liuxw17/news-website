package com.lxwtest.async.handler;

import com.lxwtest.async.EventHandler;
import com.lxwtest.async.EventModel;
import com.lxwtest.async.EventType;
import com.lxwtest.model.Message;
import com.lxwtest.model.User;
import com.lxwtest.service.MessageService;
import com.lxwtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(model.getEntityId()));
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE); //只关心点赞的行为
    }
}
