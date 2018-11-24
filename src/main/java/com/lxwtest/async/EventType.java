package com.lxwtest.async;

//表示刚刚发生了什么事件
public enum EventType {//枚举
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
