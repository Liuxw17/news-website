package com.lxwtest.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
//MailSender服务
public class MailSender implements InitializingBean { //初始化的时候就要配置好邮件的配置
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;//用模板来渲染

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("xx网站"); //发件人昵称
            InternetAddress from = new InternetAddress(nick + "<course@xxx.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();//构造一封邮件
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils //发邮件可以利用模板，利用Spring内置的Velocity引擎
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);//template对应的是写好的email模板
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception { //初始化
        mailSender = new JavaMailSenderImpl(); //JavaMailSenderImpl是java自带的

        // 请输入自己的邮箱和密码，用于发送邮件
        mailSender.setUsername("xxx@xxx.com");
        mailSender.setPassword("yyy");
        mailSender.setHost("smtp.exmail.qq.com"); //发邮件的地址
        // 请配置自己的邮箱和密码

        mailSender.setPort(465);
        mailSender.setProtocol("smtps");//协议
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties(); //属性
        javaMailProperties.put("Mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
