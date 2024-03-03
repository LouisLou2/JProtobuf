package com.github.louislou2.jprotobuf.model;

import java.util.List;

public class TypeInfo {
    String typeName;
    List<TypeInfo> typeParams;
    public String getTypeName(){
        return typeName;
    }
    public List<TypeInfo> getTypeParams(){
        return typeParams;
    }
    public void setTypeName(String typeName){
        this.typeName = typeName;
    }
    public void setTypeParams(List<TypeInfo> typeParams){
        this.typeParams=typeParams;
    }
    public void setAndNoParams(String type){
        this.typeName =type;
        this.typeParams=null;
    }
}
