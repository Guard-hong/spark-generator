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
public enum FileGenerateTypeEnum {
    DYNAMIC("动态", "dynamic"),

    STATIC("静态", "static");

    private final String text;
    private final String value;
}
