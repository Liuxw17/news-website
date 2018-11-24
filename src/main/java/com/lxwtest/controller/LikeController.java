package com.lxwtest.controller;

import com.lxwtest.async.EventModel;
import com.lxwtest.async.EventProducer;
import com.lxwtest.async.EventType;
import com.lxwtest.model.EntityType;
import com.lxwtest.model.HostHolder;
import com.lxwtest.model.News;
import com.lxwtest.service.LikeService;
import com.lxwtest.service.NewsService;
import com.lxwtest.util.NewsUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newId") int newsId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);

        // 更新喜欢数
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        //发一个事件出来
        eventProducer.fireEvent(new EventModel(EventType.LIKE)//记录点赞的事件类型
                .setEntityOwnerId(news.getUserId())//点赞的owner是谁
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)//点赞的对象是谁
        );
        return NewsUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return NewsUtil.getJSONString(0, String.valueOf(likeCount));
    }
}