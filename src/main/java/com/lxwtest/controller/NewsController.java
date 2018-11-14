package com.lxwtest.controller;

import com.lxwtest.model.HostHolder;
import com.lxwtest.model.News;
import com.lxwtest.service.NewsService;
import com.lxwtest.service.QiniuService;
import com.lxwtest.util.NewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;


@Controller
public class NewsController {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path="/image",method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(NewsUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片出错"+ e.getMessage());
        }

    }

    //某用户添加资讯
    @RequestMapping(path = {"/user/addNews"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                //设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return NewsUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯错误" + e.getMessage());
            return NewsUtil.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST}) //上传图片用POST
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //本地上传
//            String fileUrl = newsService.saveImage(file);
            //七牛云上传
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return NewsUtil.getJSONString(1, "上传图片失败");
            }
            return NewsUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return NewsUtil.getJSONString(1, "上传失败");
        }
    }
}
