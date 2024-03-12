package com.hcj.maker.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:HCJ
 * @DateTime:2024/3/11
 * @Description:
 **/
@Getter
@AllArgsConstructor
public enum ModelTypeEnum {
    STRING("字符串", "String"),
    BOOLEAN("布尔", "boolean");
    private final String text;
    private final String value;
}
