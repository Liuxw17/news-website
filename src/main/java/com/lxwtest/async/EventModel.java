package com.lxwtest.async;

import java.util.HashMap;
import java.util.Map;

//发生现场的一些数据，都打包在EventModel里面
public class EventModel {
    private EventType type;//事件类型
    private int actorId;//谁触发的
    private int entityType;//种类
    private int entityId;//Id
    private int entityOwnerId;//拥有者是谁
    private Map<String,String> exts = new HashMap<>(); //扩展信息

    public Map<String,String> getExts()
    {
        return exts;
    }
    public EventModel(){//默认的构造函数
    }
    public EventModel(EventType type){
        this.type = type;
    }
    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) { //存放扩展信息
        exts.put(name, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
