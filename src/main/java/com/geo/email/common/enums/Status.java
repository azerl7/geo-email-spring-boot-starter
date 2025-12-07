package com.geo.email.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

/**
 * @author AZERL7
 * @datetime 2025/12/6 15:42
 */

@Getter
@AllArgsConstructor
public enum Status {
    // 成功发送
    SUCCESS(1),
    // 失败发送，发送失败原因是因为邮件服务提供商的原因，请稍后重试
    FAIL(2),
    // 发送失败，失败原因是代码问题
    EXCEPTION(3)
    ;
    private final Integer code;


    public static Status valueOf(Integer code) {
        for (Status status : Status.values()) {
            if (ObjectUtils.nullSafeEquals(status.code, code)) {
                return status;
            }
        }
        return null;
    }
}
