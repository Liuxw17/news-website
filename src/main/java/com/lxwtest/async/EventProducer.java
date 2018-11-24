package com.lxwtest.async;

import com.alibaba.fastjson.JSONObject;
import com.lxwtest.util.JedisAdapter;
import com.lxwtest.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    //专门用来发送数据的（把数据序列化以后放在队列里）
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);//先序列化
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);//放到队列里
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
