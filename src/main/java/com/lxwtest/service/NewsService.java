package com.lxwtest.service;

import com.lxwtest.dao.NewsDAO;
import com.lxwtest.model.News;
import com.lxwtest.util.NewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    //获取最近的新闻
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    //增加新闻
    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    //依据id获取新闻
    public News getById(int newsId){
        return newsDAO.getById(newsId);
    }

    //存储图像
    public String saveImage(MultipartFile file) throws IOException {
        // xxx.jpg
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();//获得后缀名
        if (!NewsUtil.isFileAllowed(fileExt)) {//后缀名是否符合
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;//文件名
        Files.copy(file.getInputStream(), new File(NewsUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);//toPath()生成文件+如果存在便替换掉
        return NewsUtil.NEWS_DOMAIN + "image?name=" + fileName;
    }
}
