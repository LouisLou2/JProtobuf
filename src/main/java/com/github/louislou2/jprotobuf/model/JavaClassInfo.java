package com.github.louislou2.jprotobuf.model;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class JavaClassInfo {
    private String className;
    private List<ClassField>fields;
    public JavaClassInfo(String className, List<ClassField> fields) {
        this.className = className;
        this.fields = fields;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public List<ClassField> getFields() {
        return fields;
    }
    public void setFields(List<ClassField> fields) {
        this.fields = fields;
    }
}
