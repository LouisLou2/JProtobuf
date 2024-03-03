package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.model.ClassField;
import com.github.louislou2.jprotobuf.model.JavaClassInfo;
import com.github.louislou2.jprotobuf.model.TypeInfo;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.ArrayList;
import java.util.List;

public class PsiAnalyser {
    /**
     * 此方法目前仅支持解析psiFile文件中只有一个java类的情况
     * @param psiFile
     * @return
     */
    public static PsiClass getPsiClass(PsiFile psiFile){
        if (!(psiFile instanceof PsiJavaFile)){
            throw new RuntimeException("Not a java file");
        }
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        PsiClass[] classes = psiJavaFile.getClasses();
        if (classes.length == 0){
            throw new RuntimeException("No class in the file");
        }
        PsiClass psiClass = classes[0]; // 如果文件中有多个类，需要判断哪一个是你感兴趣的
        //TODO: 这里需要限制只有一个类
        return psiClass;
    }
    @Deprecated
    private static JavaClassInfo getJavaClassInfo(PsiClass psiClass){
        List<ClassField>fields=new ArrayList<>();
        PsiField[] psiFields=psiClass.getFields();
        for(final var psiField: psiFields) {
            PsiType fieldType = psiField.getType();
            String fieldname = psiField.getName(); // 获取字段名称
            String fieldTypeName = fieldType.getCanonicalText(); // 获取字段的完整类型名称
            String idd=fieldType.getInternalCanonicalText();
            var it=fieldType.getDeepComponentType();
            System.out.println(fieldname + ": " + fieldType.getPresentableText());

            if (fieldType instanceof PsiClassType) {
                PsiClassType classType = (PsiClassType) fieldType;
                String itt=classType.getCanonicalText();
                String ittt=classType.getClassName();
                String itttt=classType.getName();
                String ittttt=classType.getInternalCanonicalText();
                PsiClassType.ClassResolveResult classResolveResult = classType.resolveGenerics();
                PsiClass fieldClass = classResolveResult.getElement();
                List<PsiType> typeArguments = List.of(classType.getParameters());

                // 如果字段类型有泛型参数
                if (!typeArguments.isEmpty()) {
                    // 获取泛型的第一个参数（假设我们感兴趣的是第一个）
                    PsiType typeArgument = typeArguments.get(0);
                    String fieldTypeName2 = typeArgument.getCanonicalText(); // 获取字段的完整类型名称
                    // 泛型参数一定是类而非java基本对象
                    PsiClass typeArgumentClass = PsiTypesUtil.getPsiClass(typeArgument);
                    if (typeArgumentClass != null) {
                        // 打印泛型的完整类名
                        String typeArgumentQualifiedName = typeArgumentClass.getQualifiedName();
                        System.out.println("GenericType for " + fieldname + ": " + typeArgumentQualifiedName);
                    }
                }
            }
            
            PsiIdentifier id=psiField.getNameIdentifier();
            PsiType type=psiField.getType();
            String typeStr = psiField.getType().getPresentableText();
            String fieldName=psiField.getName();
            fields.add(new ClassField(fieldName, getTypeInfo(typeStr)));
        }
        return new JavaClassInfo(psiClass.getName(), fields);
    }
    
    // 预期自己解析，不如利用PsiClass, PsiField这种IDE解析好的，使用全类名更加稳妥，可惜我花大力气做的递归解析啊！！！
    @Deprecated
    public static void FieldInfomation(PsiField psiField){
        PsiType fieldType = psiField.getType();
        String fieldname = psiField.getName(); // 获取字段名称
        String fieldTypeName = fieldType.getCanonicalText(); // 获取字段的完整类型名称
        System.out.println(fieldname + ": " + fieldType.getPresentableText());

        if (fieldType instanceof PsiClassType) {
            PsiClassType classType = (PsiClassType) fieldType;
            PsiClassType.ClassResolveResult classResolveResult = classType.resolveGenerics();
            PsiClass fieldClass = classResolveResult.getElement();
            List<PsiType> typeArguments = List.of(classType.getParameters());

            // 如果字段类型有泛型参数
            if (!typeArguments.isEmpty()) {
                // 获取泛型的第一个参数（假设我们感兴趣的是第一个）
                PsiType typeArgument = typeArguments.get(0);
                String fieldTypeName2 = typeArgument.getCanonicalText(); // 获取字段的完整类型名称
                // 泛型参数一定是类而非java基本对象
                PsiClass typeArgumentClass = PsiTypesUtil.getPsiClass(typeArgument);
                if (typeArgumentClass != null) {
                    // 打印泛型的完整类名
                    String typeArgumentQualifiedName = typeArgumentClass.getQualifiedName();
                    System.out.println("GenericType for " + fieldname + ": " + typeArgumentQualifiedName);
                }
            }
        }
    }
    // 复杂度为O(nlogn),暂时没想到更高效率的方法
    @Deprecated
    private static TypeInfo getTypeInfo(String typeStr){
        int len=typeStr.length();
        TypeInfo info=new TypeInfo();
        int LtPos=typeStr.indexOf('<');
        if(LtPos==-1){
            info.setAndNoParams(typeStr);
            return info;
        }
        var type=typeStr.substring(0,LtPos);
        info.setTypeName(type);
        List<TypeInfo> typeParams=new ArrayList<>();
        if(hasOneParam(type)){// 这个if删去不影响后续结果，但是这里预先判断一下，省得
            var paramStr=typeStr.substring(LtPos+1,len-1);
            typeParams.add(getTypeInfo(paramStr));
            info.setTypeParams(typeParams);
            return info;
        }
        int count=0;
        boolean haveMeet=false;
        int begin=LtPos+1;
        int paraEnd=len-1;
        for(int i=begin;i<paraEnd;++i){
            if(count==0&&typeStr.charAt(i)==','){
                var paramStr=typeStr.substring(begin,i);
                typeParams.add(getTypeInfo(paramStr));
                count=0;
                begin=i+1+1;// 这里是得到的类型字符串中，逗号后边会多加一个空格。
                continue;
            }
            if(typeStr.charAt(i)=='<') ++count;
            else if(typeStr.charAt(i)=='>')--count;
        }
        var lastParam=typeStr.substring(begin,paraEnd);
        typeParams.add(getTypeInfo(lastParam));
        info.setTypeParams(typeParams);
        return info;
    }
    @Deprecated
    private static boolean hasOneParam(String type){
        //目前先这么着吧
        return type.equals("List")||type.equals("Set");
    }
}
