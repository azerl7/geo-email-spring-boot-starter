package com.geo.email.config;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author AZERL7
 * @datetime 2025/12/6 15:39
 */
@Data
@Component
@Validated
@ConfigurationProperties(prefix = "geo-email")
public class GeoEmailComponentProperties {
    @NotBlank(message = "SMTP服务器地址不能为空（如：smtp.163.com）")
    private String host;
    @Positive(message = "端口号必须为正数（如：465、587）")
    private Integer port;
    @NotBlank(message = "邮箱授权码不能为空")
    private String authCode;
    @NotBlank(message = "发件人邮箱地址不能为空")
    private String from;
    private boolean sslEnable=false;
    private boolean fallback=false;
    private String personal="【GEO-email-messgae】";
    private String charset="UTF-8";
}