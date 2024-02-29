package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.model.JavaClassInfo;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.List;

public class PsiAnalyser {
    public static JavaClassInfo getJavaClassInfo(PsiClass psiClass){
        List<Pair<String,String>>fields=new ArrayList<>();
        for (int i = 0; i < psiClass.getFields().length; i++) {
            fields.add(new Pair<>(psiClass.getFields()[i].getName(),psiClass.getFields()[i].getType().getPresentableText()));
        }
        return new JavaClassInfo(psiClass.getName(),fields);
    }
}
