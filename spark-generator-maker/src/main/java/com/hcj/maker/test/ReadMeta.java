package com.hcj.maker.test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.hcj.maker.meta.Meta;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
public class ReadMeta {
    public static void main(String[] args) {
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta meta = JSONUtil.toBean(metaJson, Meta.class);
        System.out.println(meta);

    }
}
