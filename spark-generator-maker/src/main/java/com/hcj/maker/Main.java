package com.hcj.maker;

import com.hcj.maker.generator.main.GenerateTemplate;
import com.hcj.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @Author:HCJ
 * @DateTime:2024/3/9
 * @Description:
 **/
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {

        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}