package com.github.louislou2.jprotobuf;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import java.io.IOException;
import java.util.Objects;

public class CustomedGroupedAction extends AnAction {
    private Project project;
    @Override
    public void actionPerformed(AnActionEvent e) {
        project=e.getProject();
        getInfoNowEditing();
        createAFile1();
        // TODO: insert action logic here
        Messages.showInfoMessage("wow", "ew");
    }
    public void getInfoNowEditing(){
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        //
        if (!(psiFile instanceof PsiJavaFile)) return;
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        PsiClass[] classes = psiJavaFile.getClasses();
        if (classes.length == 0) return;
        PsiClass psiClass = classes[0]; // 如果文件中有多个类，需要判断哪一个是你感兴趣的
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            var type=field.getType();
            // Do something with each field
            System.out.println("Field name: " + field.getName());
            System.out.println("Field type: " + field.getType());
        }
    }
    public void createAFile1(){
        String protoContent = "syntax = \"proto3\";\n\n" +
                "message Example {\n" +
                "  string name = 1;\n" +
                "}\n";
        String direc="./src/proto_model";
        VirtualFile desiredDir = ProjectRootManager.getInstance(project).getContentRoots()[0].findFileByRelativePath(direc);
        assert desiredDir != null;
        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(desiredDir);
        // 检查psiDirectory是否为空，这可能意味着VirtualFile不是目录或者目录不存在
        if(psiDirectory==null)return;
        // 创建一个新的文件内容
        String fileName = "example1.proto";

        // 使用PsiDirectory的方法来创建文件
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                // Your write operation goes here.
                PsiFile psiFile = psiDirectory.createFile(fileName);
                PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);

                // 获取一个文档对象，用于操作文件内容
                Document document = psiDocumentManager.getDocument(psiFile);
                document.setText(protoContent);
                // 提交文档更改
                psiDocumentManager.commitDocument(document);
            }
        });
        System.out.println("@@@@@@@@@@@@@@@@@@over");
    }
    public void createAFile2(){
        String protoContent = "syntax = \"proto3\";\n\n" +
                "message Example {\n" +
                "  string name = 1;\n" +
                "}\n";
        String direc="./src/proto_model";
        VirtualFile desiredDir = ProjectRootManager.getInstance(project).getContentRoots()[0].findFileByRelativePath(direc);
        assert desiredDir != null;
        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(desiredDir);
        PsiFile protoFile = PsiFileFactory.getInstance(project).createFileFromText(
                "example.proto",
                PlainTextLanguage.INSTANCE,
                protoContent
        );
        // 检查psiDirectory是否为空，这可能意味着VirtualFile不是目录或者目录不存在
        if (psiDirectory != null) {
            // 创建一个新的文件内容
            String fileName = "newFile.txt";
            String fileContent = "This is the content of the new file.";

            // 使用PsiDirectory的方法来创建文件
            PsiFile psiFile = psiDirectory.createFile(fileName);
            PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);

            // 获取一个文档对象，用于操作文件内容
            Document document = psiDocumentManager.getDocument(psiFile);

            // 更改文档内容
            if (document != null) {
                document.setText(fileContent);

                // 提交文档更改
                psiDocumentManager.commitDocument(document);
            }
        }
        //Notification notification = new Notification(
        //        "Notification.GroupId",
        //        "File Creation",
        //        "示例.proto has been created successfully.",
        //        NotificationType.INFORMATION
        //);
        //
        //Notifications.Bus.notify(notification, project);
    }
}
