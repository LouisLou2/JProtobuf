package com.github.louislou2.jprotobuf.service;

import com.intellij.psi.PsiClass;

import java.util.Map;
import java.util.function.Function;

public class SpecRules {
    public static Function<String,String> getProtoFileName;
    public static Function<String,String> getOuterClassName;
    public static Function<String,String> getMessageName;
    static{
        //TODO: 为这些函数赋值，这里只支持Java这一种语言，所以先不判断了
        getProtoFileName=JavaParser::getProtoFileName;
        getOuterClassName=JavaParser::getOuterClassName;
        getMessageName=JavaParser::getMessageName;
    }
}
