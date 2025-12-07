package com.geo.email.config;


import com.geo.email.common.exception.InitException;
import com.geo.email.utils.EmailSender;
import com.geo.email.utils.EmailUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author AZERL7
 * @datetime 2025/12/6 16:03
 */

@Configuration
@EnableConfigurationProperties({GeoEmailComponentProperties.class})
public class GeoEmailComponentAutoConfiguration {
    @Autowired
    private GeoEmailComponentProperties geoEmailComponentProperties;


    // 初始化校验：确保核心配置有效
    @PostConstruct
    public void validateConfig() {
        try {
            // 校验端口是否合法（常见邮箱端口：25、465、587）
            if (geoEmailComponentProperties.getPort() <= 0 || geoEmailComponentProperties.getPort() > 65535) {
                throw new InitException("无效的SMTP端口：" + geoEmailComponentProperties.getPort());
            }
            // 校验发件人邮箱格式
            if (!geoEmailComponentProperties.getFrom().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new InitException("发件人邮箱格式不正确：" + geoEmailComponentProperties.getFrom());
            }
        } catch (Exception e) {
            throw new InitException("邮箱组件初始化失败：" + e.getMessage());
        }
    }

    /**
     * 使用者未自定义时生效（允许用户自定义）
     * return emailSender
     */
    @Bean("emailSender")
    @ConditionalOnMissingBean
    public EmailSender emailSender() {
        EmailSender emailSender = new EmailSender();
        Properties props = new Properties();
        // SMTP服务器地址
        props.put("mail.smtp.host", geoEmailComponentProperties.getHost());
        // SSL端口
        props.put("mail.smtp.port", geoEmailComponentProperties.getPort());
        // 开启认账
        props.put("mail.smtp.auth", "true");
        // 启用SSL
        props.put("mail.smtp.ssl.enable", geoEmailComponentProperties.isSslEnable());
        // 指定 SSL 工厂类
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 显式指定SSL端口（关键补充）
        props.put("mail.smtp.socketFactory.port", geoEmailComponentProperties.getPort());
        // 不允许 fallback 到非SSL
        props.put("mail.smtp.socketFactory.fallback",geoEmailComponentProperties.isFallback());
        emailSender.setSmtpProperties(props);
        emailSender.setHost(geoEmailComponentProperties.getHost());
        emailSender.setAuthCode(geoEmailComponentProperties.getAuthCode());
        emailSender.setFrom(geoEmailComponentProperties.getFrom());
        emailSender.setCharset(geoEmailComponentProperties.getCharset());
        emailSender.setPersonal(geoEmailComponentProperties.getPersonal());
        return emailSender;
    }

    @Bean
    public EmailUtils emailUtils() {
        return new EmailUtils();
    }
}
