package com.lxwtest.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class JedisAdapter {
    public static void print(int index, Object object){
        System.out.println(String.format("%d,%s",index,object.toString()));
    }
    public static void main(String[] args){
        JedisShardInfo shardInfo = new JedisShardInfo("redis://localhost:6379/9");//连接的本地地址和端口
        shardInfo.setPassword("123456");//密码
        Jedis jedis = new Jedis(shardInfo);
        jedis.connect();//连接成功

        jedis.flushAll();

        //get与set
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        print(1,jedis.get("newhello"));
        jedis.setex("hello2",15,"world");//设置过期时间

        //
        jedis.set("pv","100");
        jedis.incr("pv");//方便用在数值相关的地方,方便高并发
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));

        //列表的操作
    }
}
