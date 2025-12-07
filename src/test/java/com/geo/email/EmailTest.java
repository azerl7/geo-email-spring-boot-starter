package com.geo.email;


import com.geo.email.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author AZERL7
 * @datetime 2025/12/6 22:43
 */

@SpringBootTest
@Slf4j
public class EmailTest {
    @Autowired
    private EmailUtils emailUtils;

    @Test
    public void sendEmail() {
//        emailUtils.sendTextEmail("1601761326@qq.com","【Geo-Email】","<b>测试</b>一下啦");
//        emailUtils.sendHtmlEmail("1601761326@qq.com","【Geo-Email】","<b>测试</b>一下啦");
    }
}
