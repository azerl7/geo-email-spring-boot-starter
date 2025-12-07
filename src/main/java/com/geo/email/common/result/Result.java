package com.geo.email.common.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AZERL7
 * @datetime 2025/12/6 15:56
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private String status;
}
