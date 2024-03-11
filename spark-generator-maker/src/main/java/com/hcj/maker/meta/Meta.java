package com.hcj.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
@NoArgsConstructor
@Data
public class Meta {
    private String name;
    private String description;
    private String basePackage;
    private String version;
    private String author;
    private String createTime;
    private FileConfig fileConfig;
    private ModelConfig modelConfig;




    @NoArgsConstructor
    @Data
    public static class FileConfig {
        private String inputRootPath;
        private String outputRootPath;
        private String sourceRootPath;
        private String type;
        private List<FileInfo> files;

        @NoArgsConstructor
        @Data
        public static class FileInfo {
            private String inputPath;
            private String outputPath;
            private String type;
            private String generateType;
            private String condition;
            private String groupKey;
            private String groupName;
            private List<FileInfo> files;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ModelConfig implements Serializable {
        private List<ModelInfo> models;

        @NoArgsConstructor
        @Data
        public static class ModelInfo implements Serializable {
            private String fieldName;
            private String type;
            private String description;
            private Object defaultValue;
            private String abbr;
            private String groupKey;
            private String groupName;
            private List<ModelInfo> models;
            private String condition;

            // 中间参数
            // 该分组下所有参数拼接字符串
            private String allArgsStr;
        }
    }


    public static void main(String[] args) {
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta bean = JSONUtil.toBean(metaJson, Meta.class);
        System.out.println(bean);
    }
}
