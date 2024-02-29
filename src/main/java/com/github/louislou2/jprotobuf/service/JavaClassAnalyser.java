package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.model.JavaClassInfo;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassAnalyser {
    private static Map<String,String>typeMap;
    private static String[] boxTypes={"Integer","Character","Long","Boolean","Float","Double","Byte","Short"};
    static{
        typeMap=new HashMap<>();//因为不会出现多个线程同时修改该map，不用担心线程安全性
        typeMap.put("int", "int32");
        typeMap.put("long", "int64");
        typeMap.put("boolean", "bool");
        typeMap.put("String", "string");
        typeMap.put("float", "float");
        typeMap.put("double", "double");
        typeMap.put("byte", "int32");
        typeMap.put("short", "int32");
        typeMap.put("char", "int32");
        typeMap.put("byte[]", "bytes");
    }
    // 此方法仅仅对于非嵌套类型，例如List<Long>中的Long, 又因为嵌套类型可能是用户自己定义的Java类，所以不能那个仅仅依靠首字母
    private static String getUnboxingType(String type){
        int typeCode=-1;
        for(int i=0;i<boxTypes.length;i++){
            if(boxTypes[i].equals(type)){
                typeCode=i;
                break;
            }
        }
        if(typeCode==-1)
            return type;
        if(typeCode==0)
            return "int";
        if(typeCode==1)
            return "char";
        return Character.toLowerCase(type.charAt(0))+type.substring(1);
    }
    //仅仅对于非嵌套类型
    private static String getProtoType(String javaType){
        return typeMap.getOrDefault(getUnboxingType(javaType), "bytes");
    }
    public static void writeJavaToProto(JavaClassInfo javaClassInfo, Project project, String corProtoPath){
        String protoContent = getProtoString(javaClassInfo);
        PlainTextFileWriter.createFile(project, corProtoPath, javaClassInfo.getClassName() + ".proto", protoContent);
    }
    // 现在还未确定message的名字和outer_classname的确定办法，先用两个暂时接口
    public static String getOuterClassName(String className){
        return className+"Proto";
    }
    public static String getMessageName(String className){
        return className+"Pro";
    }
    public static String getProtoString(JavaClassInfo javaClassInfo){
        String className=javaClassInfo.getClassName();
        String messageName=getMessageName(className);
        String outerClassName=getOuterClassName(className);
        StringBuilder protoFileContent = new StringBuilder();
        protoFileContent.append("syntax = \"proto3\";\n\n");
        protoFileContent.append("option java_outer_classname = ");
        protoFileContent.append(outerClassName);
        protoFileContent.append('\n');
        protoFileContent.append("message ").append(messageName).append(" {\n");
        List<Pair<String,String>> fields=javaClassInfo.getFields();
        for (int i = 0; i < javaClassInfo.getFields().size(); i++) {
            String fieldType = fields.get(i).getSecond();
            String protoType = getProtoType(fieldType);
            protoFileContent.append("    ").append(protoType).append(" ").append(fields.get(i).getFirst()).append(" = ").append(i + 1).append(";\n");
        }
        protoFileContent.append("}\n");
        return protoFileContent.toString();
    }
}
