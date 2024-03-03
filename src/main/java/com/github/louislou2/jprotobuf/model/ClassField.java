package com.github.louislou2.jprotobuf.model;

import java.util.List;

public class ClassField {
    private String name;// 该字段的类型
    /**
     * 做成数组形式，用于描述嵌套类型
     * 例如List<Map<Set<Short>>> 类型,typeName=[List,Map,Set,Short]
     */
    private TypeInfo type;

    public ClassField(String name, TypeInfo type) {
        this.name=name;
        this.type=type;
    }

    // setter and getter
    public String getName(){
        return name;
    }
    public TypeInfo getType(){
        return type;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setType(TypeInfo type){
        this.type=type;
    }
}
