package com.github.louislou2.jprotobuf.model;

import com.intellij.openapi.util.Pair;

import java.util.List;

/**
 * 用于表示type的总结后的信息
 */
public class TypeKind {
    private int kind;// 类型代号，遵循JavaParser中定义的
    private String selfLocation;//如果是kind==JavaParser.SELF, 这个字段才不是null
    private TypeKind param;// 只有kind==ELE1时，才不是null
    private Pair<TypeKind,TypeKind> params;// 只有是带有泛型参数的kind,此字段才不是null
    public TypeKind(int kind,String selfLocation,TypeKind param,Pair<TypeKind,TypeKind> params){
        this.kind=kind;
        this.selfLocation=selfLocation;
        this.params=params;
    }
    public static TypeKind makeTypeKind4Basic(int kind){
        return new TypeKind(kind,null,null,null);
    }
    public static TypeKind makeTypeKind4Self(int kind,String selfLocation){
        return new TypeKind(kind,selfLocation,null,null);
    }
    public static TypeKind makeTypeKind4ELE1(int kind,TypeKind param){
        return new TypeKind(kind,null,param,null);
    }
    public static TypeKind makeTypeKind4ELE2(int kind,Pair<TypeKind,TypeKind> params){
        return new TypeKind(kind,null,null,params);
    }
    public int getKind(){
        return kind;
    }
    public String getSelfLocation(){
        return selfLocation;
    }
    public Pair<TypeKind,TypeKind> getParams(){
        return params;
    }
    public void setKind(int kind){
        this.kind=kind;
    }
    public void setSelfLocation(String selfLocation){
        this.selfLocation=selfLocation;
    }
    public void setParams(Pair<TypeKind,TypeKind> params){
        this.params=params;
    }
}
