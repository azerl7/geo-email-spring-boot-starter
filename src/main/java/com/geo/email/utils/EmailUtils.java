package com.geo.email.utils;


import com.geo.email.common.dataobject.EmailContent;
import com.geo.email.common.enums.Status;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
* @author AZERL7
* @datetime 2025/12/6 16:35
*/

@Slf4j
//@DependsOn("emailSender") // 强制：先初始化emailSender，再初始化EmailUtils
public class EmailUtils implements ApplicationContextAware{

    /**
     * 静态保存 Spring 上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 静态保存 emailSender
     */
    private static volatile EmailSender emailSender;

    /**
     * 手动处理依赖
     * 调用者可以使用自定义的 sender
     * @param customEmailSender emailSender
     */
    public static void init(EmailSender customEmailSender) {
        EmailUtils.emailSender = customEmailSender;
        log.info("手动初始化EmailUtils成功");
    }

    /**
     * 仅保存Spring上下文，不做任何Bean获取操作
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EmailUtils.applicationContext = applicationContext;
        log.info("EmailUtils已获取Spring上下文，等待首次调用时加载EmailSender");
    }

    /**
     * 懒加载获取EmailSender（双重检查锁，避免多线程重复创建）
     */
    private static EmailSender getEmailSender() {
        // 第一次检查：无锁，提高性能
        if (emailSender == null) {
            synchronized (EmailUtils.class) {
                // 第二次检查：避免多线程竞争时重复初始化
                if (emailSender == null) {
                    if (applicationContext == null) {
                        throw new RuntimeException("Spring上下文未初始化，无法获取EmailSender Bean！");
                    }
                    try {
                        // 此时容器已完全初始化，能正常获取EmailSender
                        emailSender = applicationContext.getBean(EmailSender.class);
                        log.info("懒加载获取EmailSender Bean成功");
                    } catch (BeansException e) {
                        log.error("获取EmailSender Bean失败，请检查：1.自动配置类是否注册 2.geo-email配置是否完整", e);
                        throw new RuntimeException("EmailSender Bean不存在", e);
                    }
                }
            }
        }
        return emailSender;
    }

    /**
     * 发送简单文本邮件
     * @param to 收件人邮箱（多个用逗号分隔）
     * @param subject 邮件标题
     * @param content 邮件内容（纯文本）
     */
    public static Status sendTextEmail(String to, String subject, String content) {
        // 懒加载获取
        getEmailSender();
        // todo 参数校验
        // todo 异常处理
        EmailContent emailContent =EmailContent.builder()
                .head(subject)
                .body(content)
                .build();
        boolean send = emailSender.send(to, emailContent, "text/plain;charset=UTF-8");
        if (send) {
            return com.geo.email.common.enums.Status.SUCCESS;
        }
        return com.geo.email.common.enums.Status.FAIL;
    }

    /**
     * 发送HTML邮件
     * @param to 收件人邮箱
     * @param subject 邮件标题
     * @param htmlContent HTML内容
     */
    public static Status sendHtmlEmail(String to, String subject, String htmlContent) {
        // 懒加载获取
        getEmailSender();
        // todo 参数校验
        // todo 异常处理
        EmailContent emailContent =EmailContent.builder()
                .head(subject)
                .body(htmlContent)
                .build();
        boolean send = emailSender.send(to, emailContent, "text/html;charset=UTF-8");
        if (send) {
            return Status.SUCCESS;
        }
        return Status.FAIL;
    }
}
