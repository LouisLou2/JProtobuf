package com.github.louislou2.jprotobuf.model;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class JavaClassInfo {
    private String className;
    private List<Pair<String,String>>fields;
    public JavaClassInfo(String className, List<Pair<String, String>> fields) {
        this.className = className;
        this.fields = fields;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public List<Pair<String, String>> getFields() {
        return fields;
    }
    public void setFields(List<Pair<String, String>> fields) {
        this.fields = fields;
    }
}
