package com.lxwtest.async;

import java.util.List;

//接口
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
