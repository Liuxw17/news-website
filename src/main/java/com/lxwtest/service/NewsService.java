package com.lxwtest.service;

import com.lxwtest.dao.NewsDAO;
import com.lxwtest.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset,  int limit){//服务里定义的接口尽可能通用些
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
}

