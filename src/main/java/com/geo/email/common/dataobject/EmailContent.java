package com.geo.email.common.dataobject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AZERL7
 * @datetime 2025/12/6 15:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailContent {
    String head;
    String body;
}
