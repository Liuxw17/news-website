package com.lxwtest.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {//初始化以后
        //jedis = new Jedis("localhost");
        pool = new JedisPool("localhost", 6379);
    }

    private Jedis getJedis() {
        //return jedis;
        return pool.getResource();
    }

    public long sadd(String key, String value) {//集合里增加
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {//集合里取消
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {//看看集合里有多少人
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }




    public static void print(int index, Object object) {
        System.out.println(String.format("%d,%s", index, object.toString()));
    }

    public static void main(String[] args) {
        JedisShardInfo shardInfo = new JedisShardInfo("redis://localhost:6379");//连接的本地地址和端口
        shardInfo.setPassword("123456");//密码
        Jedis jedis = new Jedis(shardInfo);
        jedis.connect();//连接成功

        jedis.flushAll();

        //get与set
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("hello2", 15, "world");//设置过期时间

        //
        jedis.set("pv", "100");
        jedis.incr("pv");//方便用在数值相关的地方,方便高并发
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));

        //列表的操作
        String listName = "listA";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(listName, 0, 12));//在0-12这个范围内
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));
        print(8, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(9, jedis.lrange(listName, 0, 12));

        // hash, 可变字段
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18666666666");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zju");//不存在的话才设置，存在的话不设置
        jedis.hsetnx(userKey, "name", "yxy");
        print(19, jedis.hgetAll(userKey));

        // 集合，点赞用户群, 共同好友
        String likeKey1 = "newsLike1";
        String likeKey2 = "newsLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * 2));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));//求并集
        print(23, jedis.sdiff(likeKey1, likeKey2));//求不同的
        print(24, jedis.sinter(likeKey1, likeKey2));//求交集
        print(25, jedis.sismember(likeKey1, "12"));
        print(26, jedis.sismember(likeKey2, "12"));
        jedis.srem(likeKey1, "5");//删除
        print(27, jedis.smembers(likeKey1));
        // 从1移动到2
        jedis.smove(likeKey2, likeKey1, "14");
        print(28, jedis.smembers(likeKey1));
        print(29, jedis.scard(likeKey1));//求长度

        // 排序集合，有限队列，排行榜
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 61, 100));//在61-100之间的人数
        // 改错卷了
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc"));
        print(35, jedis.zcount(rankKey, 0, 100));
        // 1-4 名 Luc
        print(36, jedis.zrange(rankKey, 0, 10));
        print(36, jedis.zrange(rankKey, 1, 3));
        print(36, jedis.zrevrange(rankKey, 1, 3));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }

        print(38, jedis.zrank(rankKey, "Ben"));
        print(39, jedis.zrevrank(rankKey, "Ben"));

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(42, jedis.zlexcount(setKey, "[b", "[d"));
        jedis.zrem(setKey, "b");
        print(43, jedis.zrange(setKey, 0, 10));
        jedis.zremrangeByLex(setKey, "(c", "+");
        print(44, jedis.zrange(setKey, 0, 2));

//        JedisPool pool = new JedisPool();//连接池
//        for (int i = 0; i < 100; ++i) {
//            Jedis j = pool.getResource();
//            j.get("a");
//            System.out.println("POOL:" + i);
//            j.close();
//        }
    }
}
