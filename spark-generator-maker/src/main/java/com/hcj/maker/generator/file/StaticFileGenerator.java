package com.hcj.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
public class StaticFileGenerator {

    /**
     * 复制静态文件
     *
     * @param inputPath 输入文件路径
     * @param outputPath 输出文件路径
     */
    public static void copyFileByHuTool(String inputPath,String outputPath){
        FileUtil.copy(inputPath,outputPath,false);
    }
}
