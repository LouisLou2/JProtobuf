package com.github.louislou2.jprotobuf;

import com.github.louislou2.jprotobuf.model.JavaClassInfo;
import com.github.louislou2.jprotobuf.service.JavaClassAnalyser;
import com.github.louislou2.jprotobuf.service.PathManager;
import com.github.louislou2.jprotobuf.service.ProtoGenerator;
import com.github.louislou2.jprotobuf.service.PsiAnalyser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

public class EditerPopForCurrentFile extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //Messages.showInfoMessage("wow", "uu");
        //ProtoGenerator.writeProtoClassByProtoFile("","","");
        Project project=e.getProject();
        PsiFile psiFileNowEditing = getPsiFileNowEditing(project);
        String nowEditingPath=psiFileNowEditing.getVirtualFile().getPath();
        JavaClassInfo javaClassInfo = getInfo(psiFileNowEditing);
        ProtoGenerator.writeProtoClassFile_Default(javaClassInfo,project,nowEditingPath);
        
        
        //PathManager.testPath(project);
        //VirtualFile nowEditingVir= psiFileNowEditing.getVirtualFile();
        //String corProtoDir=PathManager.getCorProtoDir(nowEditingPath);
        //JavaClassAnalyser.writeJavaToProto(javaClassInfo,project,corProtoDir);
    }
    public PsiFile getPsiFileNowEditing(Project project){
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        return psiFile;
    }
    public JavaClassInfo getInfo(PsiFile psiFile){
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
        return PsiAnalyser.getJavaClassInfo(psiClass);
    }
}
