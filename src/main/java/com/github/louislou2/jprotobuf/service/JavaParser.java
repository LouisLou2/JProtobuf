package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.model.ClassField;
import com.github.louislou2.jprotobuf.model.TypeInfo;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaPrimitiveType;
import org.hamcrest.core.Is;
import org.jaxen.util.SelfAxisIterator;

import java.util.*;

public class JavaParser {
    private static Map<String, String> typeMap;
    private static final Set<String> boxTypes;
    private static final int lastNameInd=10;
    private static final Set<String>baseTypes;
    private static final String list="java.util.List";
    private static final String set="java.util.Set";
    private static final String vector="java.util.Vector";
    private static final String map="java.util.Map";
    private static final int BASE=0; // 基本数据类型
    private static final int BOX=1; // 基本数据类型包装类
    private static final int SELF=2; // 自定义类
    private static final int ELE1=3; // 包含一个类型参数的Collection
    private static final int KV=4; //键值对集合，各种Table
    
    static{
        typeMap=new HashMap<>();//因为不会出现多个线程同时修改该map，不用担心线程安全性
        typeMap.put("int", "int32");
        typeMap.put("long", "int64");
        typeMap.put("boolean", "bool");
        typeMap.put("string", "string");//是的这里java中应该是String,这里暂且将java.lang.String视作string的包装类吧
        typeMap.put("float", "float");
        typeMap.put("double", "double");
        typeMap.put("byte", "int32");
        typeMap.put("short", "int32");
        typeMap.put("char", "int32");
        //typeMap.put("byte[]", "bytes"); 这中原始数组还没有制定方案
        boxTypes=new HashSet<>();
        boxTypes.addAll(List.of(
                "java.lang.Integer",
                "java.lang.Character",
                "java.lang.Long",
                "java.lang.Boolean",
                "java.lang.Float",
                "java.lang.Double",
                "java.lang.Byte",
                "java.lang.Short",
                "java.lang.String"
        ));
        baseTypes=new HashSet<>();
        baseTypes.addAll(List.of(
                "int","long","boolean","String","float","double","byte","short","char"
        ));
    }
    private static boolean isStructured(int kind){
        return kind==ELE1||kind==KV;
    }
    /**
     * 包装类转基本类，例如java.lang.Integer->int
     * 注意使用此类前已经确定他是一个严格书写正确的包装类的全类名，也就是说，属于boxTypes集合
     * @param canonicalName
     * @return
     */
    private static String getUnboxingType(String canonicalName){
        if(canonicalName.charAt(lastNameInd)=='I')return "int";
        if(canonicalName.charAt(lastNameInd)=='C')return "char";
        return Character.toLowerCase(canonicalName.charAt(lastNameInd)) + canonicalName.substring(lastNameInd+1);
    }

    /**
     * 此方法仅仅用来处理[基本数据类型][包装类]以及[用户自定义类]
     * 其他均不服务
     * @param type
     * @param kind
     * @return
     */
    private static String getSimpleProtoType(PsiType type,int kind){
        String canonicalName=type.getCanonicalText();
        assert kind==BASE||kind==BOX||kind==SELF;
        if(kind==SELF){
            return getMessageName(type.getPresentableText());
        }
        String name=kind==BASE?canonicalName:getUnboxingType(canonicalName);
        return typeMap.get(name);
    }
    /**
     * 0：基本数据类型
     * 1：包装类
     * 2：用户自定义类
     * 3：线性表或集合
     * 4：键值对集合(Map)
     * TODO:这里也许可以考虑将他们做成常量,一会再说吧
     * @return
     */
    public static int getKind(PsiType type){
        // 注意是PsiPrimitiveType不是JavaPrimitiveType
        if(type instanceof PsiPrimitiveType){
            //表明是基本类型
            return BASE;
        }
        if(boxTypes.contains(type.getCanonicalText())){
            // 表明是基本类型的包装类
            return BOX;
        }
        PsiClassType classType=(PsiClassType)type;
        PsiClass classInfo=classType.resolve();
        assert classInfo != null;
        String outerCanon=classInfo.getQualifiedName();
        if(outerCanon==list||outerCanon==vector||outerCanon==set)
            return ELE1;
        if(outerCanon==map)
            return KV;
        /*
        说明可能是用户自定义类，但是这里也有Java其他数据结构的嫌疑，
        因为在上面判断的数据结构类型只有几个.这里姑且当做就是用户自定义类吧
        TODO: 进一步判断
         */
        return SELF;
    }

    /**
     * 在已知该PsiTyte只能是BOX或者SELF的情况下调用
     * @param type
     * @return
     */
    //public static String getStrKind4BoxAndSelf(PsiType type){
    //    int kind = boxTypes.contains(type.getCanonicalText())?BOX:SELF;
    //    return getSimpleProtoType(type.getCanonicalText(),kind);
    //}

    /**
     * 在已知该PsiTyte只能是BOX或者SELF的情况下调用
     * @param type
     * @return
     */
    public static int getKind4BoxAndSelf(PsiType type){
        return boxTypes.contains(type.getCanonicalText())?BOX:SELF;
    }
    public static String getProtoString(PsiClass classInfo){
        String className=classInfo.getName();
        String messageName=getMessageName(className);
        String outerClassName=getOuterClassName(classInfo);
        StringBuilder header = new StringBuilder();
        StringBuilder content= new StringBuilder();
        content.append("message ").append(messageName).append(" {");
        var fields=classInfo.getFields();
        Set<String>selfClasses=new HashSet<>();
        String[] selfs;
        for(int i=0;i<fields.length;++i){
            selfs=parseStr4Nesting(fields[i], content, i+1);
            for(var self:selfs){
                if(self!=null)selfClasses.add(self);
            }
        }
        content.append("\n}");
        header.append("syntax = \"proto3\";\n\n");
        selfClasses.forEach(self-> header.append("import \"").append(self).append("\";\n"));
        header.append("\noption java_outer_classname = ");
        header.append('\"'+outerClassName+"\";\n\n");
        return header +content.toString();
    }

    /**
     * 此类目前只能解决不嵌套或者一层数据结构嵌套
     * @param field
     * @param builder
     * @param order
     */
    // TODO:还未做.protobuf的import语句
    private static String[] parseStr4Nesting(PsiField field, StringBuilder builder, int order){
        assert fieldPromise(field);
        String[]selfs=new String[2];// 最多出现两个需要import的SELF
        builder.append("\n    ");
        
        String fieldName=field.getName();
        PsiType type=field.getType();
        int kind=getKind(type);
        if(isStructured(kind)){
            PsiClassType classType=(PsiClassType)type;
            PsiClass classInfo=classType.resolve();
            assert classInfo != null;
            //String outerCanon=classInfo.getQualifiedName();
            PsiType[] typeParams=classType.getParameters();
            switch(kind){
                case ELE1->{
                    // ELE1种类他们的泛型参数只有1个
                    assert typeParams.length==1;
                    int parakind=getKind4BoxAndSelf(typeParams[0]);
                    String protoType=getSimpleProtoType(typeParams[0],parakind);
                    if(parakind==SELF) selfs[0]=PathManager.getRelaCorProtoPath(PsiUtil.resolveClassInType(typeParams[0]),JavaParser::getProtoFileName);
                    builder.append("repeated ").append(protoType).append(" ").append(fieldName);
                }
                case KV->{
                    assert typeParams.length==2;
                    int kind0=getKind4BoxAndSelf(typeParams[0]);
                    int kind1=getKind4BoxAndSelf(typeParams[1]);
                    
                    if(kind0==SELF) selfs[0]=PathManager.getRelaCorProtoPath((PsiClass)typeParams[0],JavaParser::getProtoFileName);
                    if(kind1==SELF) selfs[1]=PathManager.getRelaCorProtoPath((PsiClass)typeParams[1],JavaParser::getProtoFileName);
                    
                    String protoType1=getSimpleProtoType(typeParams[0],kind0);
                    String protoType2=getSimpleProtoType(typeParams[1],kind1);
                    builder.append("map<").append(protoType1).append(", ").append(protoType2).append("> ").append(fieldName);
                }
                default -> {
                    // this is unbearable and impossible;
                }
            }
        }else{
            String baseProtoType=getSimpleProtoType(type,kind);
            builder.append(baseProtoType).append(" ").append(fieldName);
            if(kind==SELF){
                selfs[0]=type.getCanonicalText();
                selfs[1]=null;
            }
        }
        builder.append(" = ").append(order).append(';');
        return selfs;
    }

    /**
     * 因为太过复杂的java解析还做不到，先从简单地入手
     * 这个方法定义ClassField的限制：
     * 1.最多支持一层嵌套即例如Map<Integer,String>
     * @param field
     * @return
     */
    private static boolean fieldPromise(PsiField field){
        PsiType type=field.getType();
        int kind=getKind(type);
        if(isStructured(kind)){
            PsiClassType classType=(PsiClassType)type;
            PsiClass classInfo=classType.resolve();
            assert classInfo != null;
            PsiType[] typeParams=classType.getParameters();
            for(var ty:typeParams){
                if(isStructured(getKind(ty)))return false;
            }
        }
        return true;
    }
    /**
     * @param classInfo
     * @return .proto文件中java_outer_classname的名字，目前还没有书写用户如何指定，先用以下策略
     */
    public static String getOuterClassName(PsiClass classInfo){
        return classInfo.getName()+"Proto";
    }

    /**
     * .proto文件中message的名字，目前还没有书写用户如何指定，先用以下策略
     * @param className
     * @return
     */
    public static String getMessageName(String className){
        return className+"Pro";
    }

    /**
     * @param classInfo
     * @return .proto文件的名字，目前还没有书写用户如何指定，先用以下策略
     */
    public static String getProtoFileName(PsiClass classInfo){
        return classInfo.getName()+".proto";
    }
}
