package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class PlainTextFileWriter {
    public static void createFile(Project proj, String folder, String fileName, String content){
        VirtualFile desiredDir = PathVirtualUtil.getVF(folder);
        if(desiredDir==null){
            PathVirtualUtil.createDirIfNonexistent(folder);
            desiredDir=PathVirtualUtil.refreshAndGetVF(folder);
        }
        // 强制 VFS 立即更新其状态
        //VirtualFileManager.getInstance().syncRefresh();
        PsiDirectory psiDirectory = PsiManager.getInstance(proj).findDirectory(desiredDir);
        if(psiDirectory==null){
            //TODO: 此时应该发出error的notification并中断该操作
            return;
        }
        ApplicationManager.getApplication().runWriteAction(()-> {
            // Your write operation goes here.
            PsiFile psiFile = psiDirectory.createFile(fileName);
            PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(proj);

            // 获取一个文档对象，用于操作文件内容
            Document document = psiDocumentManager.getDocument(psiFile);
            assert document != null;
            document.setText(content);
            /* 提交文档更改:commitDocument方式会把修改责任交给平台，
            所以对于文件系统的修改可能不会立即生效，当接下来的操作不会立即依赖这个文件，
            使用commitDocument就够了
             */
            // psiDocumentManager.commitDocument(document);
            // 显式保存 Document 以确保立刻写入到文件
            FileDocumentManager.getInstance().saveDocument(document);
        });
        
    }
}
