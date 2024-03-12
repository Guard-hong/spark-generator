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
public enum FileTypeEnum {

    DIR("目录", "dir"),
    FILE("文件", "file"),
    GROUP("文件组", "group");
    private final String text;
    private final String value;
}
