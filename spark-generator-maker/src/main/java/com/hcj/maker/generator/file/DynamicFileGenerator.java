package com.hcj.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import com.hcj.maker.meta.Meta;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
public class DynamicFileGenerator {

    /**
     * 生成文件
     *
     * @param inputPath 模板文件输入路径
     * @param outputPath 输出路径
     * @param model 数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Meta model) throws IOException, TemplateException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件所在的路径
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        configuration.setEncoding(Locale.getDefault(),"UTF-8");

        // 创建模板对象，加载指定模板
        Template template = configuration.getTemplate(new File(inputPath).getName(), "UTF-8");
        if(!FileUtil.exist(outputPath)){
            FileUtil.touch(outputPath);
        }
        // 5. 输出文件
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(outputPath)), StandardCharsets.UTF_8));
        template.process(model, out);
        // 6. 关闭流
        out.close();
    }
}
