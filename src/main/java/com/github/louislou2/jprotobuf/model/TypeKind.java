package com.github.louislou2.jprotobuf.model;

import com.intellij.openapi.util.Pair;

/**
 * 用于表示type的总结后的信息
 */
public class TypeKind {
    private int kind;// 类型代号，遵循JavaParser中定义的
    private String defLoca;//如果是kind==JavaParser.SELF, 这个字段才不是null
    private TypeKind param;// 只有kind==ELE1时，才不是null
    private Pair<TypeKind,TypeKind> params;// 只有是带有泛型参数的KV,此字段才不是null
    public TypeKind(int kind, String defLoca, TypeKind param, Pair<TypeKind,TypeKind> params){
        this.kind=kind;
        this.defLoca = defLoca;
        this.params=params;
    }
    public static TypeKind makeTypeKind4Basic(int kind){
        return new TypeKind(kind,null,null,null);
    }
    public static TypeKind makeTypeKind4Define(int kind, String selfLocation){
        return new TypeKind(kind,selfLocation,null,null);
    }
    public static TypeKind makeTypeKind4ELE1(int kind,TypeKind param){
        return new TypeKind(kind,null,param,null);
    }
    public static TypeKind makeTypeKind4KV(int kind, Pair<TypeKind,TypeKind> params){
        return new TypeKind(kind,null,null,params);
    }
    public int getKind(){
        return kind;
    }
    public String getDefLoca(){
        return defLoca;
    }
    public TypeKind getParam(){return param;}
    public Pair<TypeKind,TypeKind> getParams(){
        return params;
    }
    public void setKind(int kind){
        this.kind=kind;
    }
    public void setDefLoca(String defLoca){
        this.defLoca = defLoca;
    }
    public void setParam(TypeKind param){this.param=param;}
    public void setParams(Pair<TypeKind,TypeKind> params){
        this.params=params;
    }
}
