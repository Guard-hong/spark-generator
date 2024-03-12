package com.hcj.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.hcj.maker.template.enums.FileFilerRuleEnum;
import com.hcj.maker.template.enums.FileFilterRangeEnum;
import com.hcj.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:HCJ
 * @DateTime:2024/3/12
 * @Description:
 **/
public class FileFilter {

    /**
     * 对某个文件或目录进行过滤，返回文件列表
     *
     * @param filePath
     * @param fileFileterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFileterConfigList){
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> doSingleFileFilter(fileFileterConfigList,file))
                .collect(Collectors.toList());
    }

    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigList 过滤规则
     * @param file 单个文件
     * @return 是否保留
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        boolean result = true;

        if(CollUtil.isEmpty(fileFilterConfigList)){
            return result;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if(fileFilterRangeEnum ==null){
                continue;
            }

            // 过滤内容，默认为类名
            String content = fileName;
            switch (fileFilterRangeEnum){
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }


            // 过滤规则
            FileFilerRuleEnum fileFilerRuleEnum = FileFilerRuleEnum.getEnumByValue(rule);
            if (fileFilerRuleEnum == null){
                continue;
            }
            switch (fileFilerRuleEnum){
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                default:
            }
            if(!result){
                return result;
            }
        }
        return result;
    }
}
