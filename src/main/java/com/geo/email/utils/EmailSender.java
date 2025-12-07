package com.geo.email.utils;


import com.geo.email.common.dataobject.EmailContent;
import com.geo.email.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author AZERL7
 * @datetime 2025/12/6 16:01
 */

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSender {
    private Properties smtpProperties;
    private String from;
    private String authCode;
    private String host;
    private String personal;
    private String charset;

    /**
     * 发送邮箱信息
     * @param to 接收者邮箱
     * @param content 邮件格式
     * @return status  是否发送成功
     */
    public boolean send(String to, EmailContent content,String type) {
        // 1、参数校验
        if (to == null || to.trim().isEmpty()) {
            log.error("收件人邮箱不能为空");
            return false;
        }
        if (content == null || content.getHead() == null || content.getBody() == null) {
            log.error("邮件标题或内容不能为空");
            return false;
        }
        // 2、 创建会话（开启调试模式，查看详细交互日志）（使用 javax.mail.Session）
        Session session = Session.getInstance(smtpProperties, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                // 打印认证信息（仅调试用，上线后删除）
                System.out.println("认证信息：" + from + " | 授权码长度：" + authCode.length());
                return new javax.mail.PasswordAuthentication(from, authCode);
            }
        });
        // 调试模式，仅仅在 debug 模式生效
        session.setDebug(log.isDebugEnabled());

        try {
            // 3、 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from,personal,charset));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(content.getHead(), "UTF-8");
            message.setContent(content.getBody(), type);
            message.setSentDate(new Date());
            message.saveChanges();

            // 4、发送邮件
            Transport.send(message);
            log.info("==> 邮件发送成功，收件人：{}，标题：{}", to, content.getHead());
            return true;
        } catch (Exception e) {
            // 打印完整异常堆栈（关键：包含服务器返回的错误码）
            System.err.println("发送失败，详细错误：");
            e.printStackTrace();
            log.error("邮件发送失败，收件人：{}，错误信息：{}", to, e.getMessage(), e);
            return false;
        }
    }
}
