package com.hcj.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.hcj.maker.meta.Meta;
import com.hcj.maker.meta.enums.FileGenerateTypeEnum;
import com.hcj.maker.meta.enums.FileTypeEnum;
import com.hcj.maker.template.model.TemplateMakerConfig;
import com.hcj.maker.template.model.TemplateMakerFileConfig;
import com.hcj.maker.template.model.TemplateMakerModelConfig;
import com.hcj.maker.template.model.TemplateMakerOutputConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:HCJ
 * @DateTime:2024/3/12
 * @Description: 模板制作工具
 **/
public class TemplateMaker {
    /**
     * 工作空间的目录
     */
    public static final String WORKSPACE_DIRECTORY = ".temp";
    /**
     * 模板文件的后缀
     */
    public static final String TEMPLATE_FILE_SUFFIX = ".ftl";
    /**
     * 元信息名称
     */
    public static final String META_INFORMATION_NAME = "meta.json";



    /**
     * 制作模板
     *
     * @param templateMakerConfig
     * @return
     */
    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig templateMakerFileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig templateMakerModelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutputConfig templateMakerOutputConfig = templateMakerConfig.getOutputConfig();
        Long id = templateMakerConfig.getId();

        return makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, templateMakerOutputConfig, id);
    }

    /**
     * 制作模板
     *
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param templateMakerOutputConfig
     * @param id
     * @return
     */
    private static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, TemplateMakerOutputConfig templateMakerOutputConfig, Long id) {
        if(id == null){
            id = IdUtil.getSnowflakeNextId();
        }
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + WORKSPACE_DIRECTORY;
        String templatePath = tempDirPath + File.separator + id;

        // 判断是否首次制作模板
        // 目录不存在者制作
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath,true);
        }

        // 1. 输入信息
        // 输入文件信息，获取到项目根目录
        String sourceRootPath = FileUtil.normalize(FileUtil.loopFiles(new File(templatePath),1,null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath());

        // 2. 制作文件模板
        List<Meta.FileConfig.FileInfo> newFileInfoList = makeFileMultipleFileTemplate(templateMakerFileConfig, templateMakerModelConfig, sourceRootPath);
        // todo 处理模型信息
        // todo 生成元信息文件 meta.json
        return id;
    }

    /**
     * 制作多个模板文件
     *
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> makeFileMultipleFileTemplate(TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath) {
        ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        if(templateMakerFileConfig == null){
            return newFileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();
        if(CollUtil.isEmpty(fileInfoConfigList)){
            return newFileInfoList;
        }

        // 生成文件模板
        // 遍历输入文件
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();

            // 如果填写的相对路径，更改成绝对路径
            if(!inputFilePath.startsWith(sourceRootPath)){
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

            // 获取过滤后的文件列表（不会存在目录）
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigList());
            fileList = fileList.stream()
                    .filter(file -> !file.getAbsolutePath().endsWith(TEMPLATE_FILE_SUFFIX))
                    .collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeSingleFileTemplate(templateMakerModelConfig, sourceRootPath, file, fileInfoConfig);
                newFileInfoList.add(fileInfo);
            }
        }
        // todo 分组处理
        return newFileInfoList;
    }

    /**
     * 制作单个文件模板
     *
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param inputFile
     * @param fileInfoConfig
     * @return
     */
    private static Meta.FileConfig.FileInfo makeSingleFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile, TemplateMakerFileConfig.FileInfoConfig fileInfoConfig) {
        // 输入文件的绝对路径
        String fileInputAbsolutePath = FileUtil.normalize(inputFile.getAbsolutePath());
        // 输入文件变成模板文件后保存的路径
        String fileOutputAbsolutePath = FileUtil.normalize(fileInputAbsolutePath + TEMPLATE_FILE_SUFFIX);


        //输入文件的路径
        String fileInputPath = FileUtil.normalize(fileInputAbsolutePath.replace(sourceRootPath + "/", ""));
        // 输出文件路径
        String fileOutputPath = FileUtil.normalize(fileInputPath + TEMPLATE_FILE_SUFFIX);

        // 使用字符串替换，生成模板文件
        String fileContent;
        // 如果存在，则操作 .ftl 模板文件，不存在则生成
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if(hasTemplateFile){
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        }else{
            fileContent = FileUtil.readUtf8String(fileInputPath);
        }

        // todo 当前一个json文件中所有的文件都归属于一个分组
        // 支持多个模型：对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            String fieldName = modelInfoConfig.getFieldName();
            if(modelGroupConfig == null){
                replacement = String.format("${%s}", fieldName);
            }else {
                replacement = String.format("${%s.%s}", modelGroupConfig.getGroupKey(), fieldName);
            }

            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        // 注意文件输入路径要和输出路径反转
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // 是否更改了文件内容
        boolean contentEquals = newFileContent.equals(fileContent);
        // 之前不存在模板文件，并且没有更改文件内容，则为静态生成
        if (!hasTemplateFile) {
            if (contentEquals) {
                // 输入路径没有 FTL 后缀
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                // 没有模板文件，需要挖坑，生成模板文件
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            // 有模板文件，且增加了新坑，生成模板文件
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }

        return fileInfo;
    }
}
