package com.hcj.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
public class MetaManager {
    private static volatile Meta meta;

    private MetaManager(){}

    public static Meta getMetaObject(){
        if(meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta meta = JSONUtil.toBean(metaJson, Meta.class);
        // 填充
        MetaValidator.doValidAndFill(meta);
        return meta;
    }
}
