package com.zenas.mall.mbg;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 *  generator 的 自定义生成器
 */
public class CommentGenerator extends DefaultCommentGenerator {
    public boolean addRemarksComments = false;


    /**
     *  设置用户配置的参数
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.addRemarksComments= StringUtility.isTrue(properties.getProperty("addRemarkComments"));
    }

    /**
     *  设置注释 给字段增加注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remarks=introspectedColumn.getRemarks();
        //根据参数和备注信息是否添加备注信息
        if(addRemarksComments && StringUtility.stringHasValue(remarks)){
            addFieldJavaDoc(field,remarks);
        }
    }

    /**
     * 给model的字段增加注释
     */
    public void  addFieldJavaDoc(Field field,String remarks){
        //文档注释开始
        field.addJavaDocLine("/**");
        //获取数据库的字段的备注信息
        String[] remarkLines=remarks.split(System.getProperty("line.separator"));
        for (String remarkLine : remarkLines) {
            field.addJavaDocLine("*"+remarkLine);
        }
        addJavadocTag(field,false);
        field.addJavaDocLine("*/");
    }
}
