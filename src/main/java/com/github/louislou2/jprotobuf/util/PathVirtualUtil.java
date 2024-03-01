package com.github.louislou2.jprotobuf.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.io.IOException;
import java.util.Stack;

public class PathVirtualUtil {
    // 这里使用这两个接口包装是因为，VirtualFile本身就是应对各种文件系统，不只是本地，但是除了本地，其他的我还没有用过，所以这里先准备好接口，今后有机会再改
    public static VirtualFile getVF(String filePath) {
        return LocalFileSystem.getInstance().findFileByPath(filePath);
    }
    public static VirtualFile refreshAndGetVF(String filePath) {
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath);
    }
    public static VirtualFile getVFWithinProject(String relativePath, Project proj) {
        var manager= ProjectRootManager.getInstance(proj);
        if(manager==null){
            return null;
        }
        var roots=manager.getContentRoots();
        if(roots.length == 0){
            return null;
        }
        var root=roots[0];
        return root.findFileByRelativePath(relativePath);
    }
    public static boolean isFileInDirectory(VirtualFile file, VirtualFile directory) {
        String filePath = file.getPath();
        String directoryPath = directory.getPath();
        return filePath.startsWith(directoryPath);
    }
    public static String getRelativePath(VirtualFile file, VirtualFile directory) {
        String filePath = file.getPath();
        String directoryPath = directory.getPath();
        if (isFileInDirectory(file, directory)) {
            return filePath.substring(directoryPath.length());
        } else {
            return null;
        }
    }
    public static boolean isFileExists(String filePath) {
        //VirtualFileManager.getInstance().findFileByUrl(filePath);：这个方法接受一个 URL 格式的文件路径作为参数。它可以处理各种协议的 URL，包括文件系统路径（以 file:// 开头）、jar 文件路径（以 jar:// 开头）等
        VirtualFile virtualFile = getVF(filePath);
        return virtualFile != null && virtualFile.exists();
    }
    //public static void createDirIfNonexistent1(Project project, String directoryPath) {
    //    ApplicationManager.getApplication().runWriteAction(() -> {
    //        try {
    //            // 获取本地文件系统的实例
    //            LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
    //
    //            // 通过本地文件系统获取VirtualFile
    //            VirtualFile directory = localFileSystem.refreshAndFindFileByPath(directoryPath);
    //            if(directory!=null&&!directory.exists()){
    //                return;
    //            }
    //            String[] pathSegments = directoryPath.split("/");
    //            StringBuilder pathBuilder = new StringBuilder();
    //
    //            for (String segment : pathSegments) {
    //                pathBuilder.append(segment);
    //                // 检查并创建缺失的目录
    //                String nowpath=pathBuilder.toString();
    //                VirtualFile currentDir = localFileSystem.refreshAndFindFileByPath(pathBuilder.toString());
    //                if (currentDir == null || !currentDir.exists()) {
    //                    assert currentDir.getParent() != null;
    //                    currentDir.getParent().createChildDirectory(project, segment);
    //                }
    //                pathBuilder.append('/');
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    });
    //}
    public static void createDirIfNonexistent(/*Project project, */String directoryPath){
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                Stack<String> stack=new Stack<>();
                String path=directoryPath;
                int lastSlashIndex=path.lastIndexOf("/");
                do{
                    if(isFileExists(path)){
                        break;
                    }
                    stack.push(path.substring(lastSlashIndex+1));
                    path=path.substring(0,lastSlashIndex);
                    lastSlashIndex=path.lastIndexOf("/");
                }while(lastSlashIndex!=-1);
                while(!stack.isEmpty()){
                    String segment=stack.pop();
                    VirtualFile currentDir = refreshAndGetVF(path);
                    //TODO: 这里的requestor原本指定了是一个project, 但是感觉没什么必要，之后再说吧
                    currentDir.createChildDirectory(null,segment);
                    path=path+"/"+segment;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    // 以上两个方法定位同一个文件得到的VirtualFile引用应该是一样的，就是夏敏这两个引用那个斌两hashCode一样:
    //String direc="./src/Main.java";
    //String direc2="D:/SourceCode/ProcessProtoc/src/Main.java";
    //var vf=ProjectRootManager.getInstance(project).getContentRoots()[0].findFileByRelativePath(direc);
    //var vf2= LocalFileSystem.getInstance().findFileByPath(direc2);
    //int hash1=vf.hashCode();
    //int hash2=vf2.hashCode();
    //assert hash1==hash2;
}
