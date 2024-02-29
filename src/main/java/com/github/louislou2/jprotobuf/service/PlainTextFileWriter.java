package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class PlainTextFileWriter {
    public static void createFile(Project proj, String folder, String fileName, String content){
        VirtualFile desiredDir = PathVirtualUtil.getVF(folder);
        if(desiredDir==null){
            //TODO: 此时应该发出error的notification并中断该操作
            return;
        }
        PsiDirectory psiDirectory = PsiManager.getInstance(proj).findDirectory(desiredDir);
        if(psiDirectory==null){
            //TODO: 此时应该发出error的notification并中断该操作
            return;
        }
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                // Your write operation goes here.
                PsiFile psiFile = psiDirectory.createFile(fileName);
                PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(proj);

                // 获取一个文档对象，用于操作文件内容
                Document document = psiDocumentManager.getDocument(psiFile);
                document.setText(content);
                // 提交文档更改
                psiDocumentManager.commitDocument(document);
            }
        });
    }
}
