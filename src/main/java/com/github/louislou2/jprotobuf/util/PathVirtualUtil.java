package com.github.louislou2.jprotobuf.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

public class PathVirtualUtil {
    public static VirtualFile getVF(String absolutPath) {
        // 通过 LocalFileSystem 获取 VirtualFile 对象
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(absolutPath);
        if (virtualFile != null) {
            // 文件找到，执行操作...
            System.out.println("File found: " + virtualFile.getPath());
        } else {
            // 文件没有找到，处理错误...
            System.out.println("File not found");
        }
        return virtualFile;
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
    // 以上两个方法定位同一个文件得到的VirtualFile引用应该是一样的，就是夏敏这两个引用那个斌两hashCode一样:
    //String direc="./src/Main.java";
    //String direc2="D:/SourceCode/ProcessProtoc/src/Main.java";
    //var vf=ProjectRootManager.getInstance(project).getContentRoots()[0].findFileByRelativePath(direc);
    //var vf2= LocalFileSystem.getInstance().findFileByPath(direc2);
    //int hash1=vf.hashCode();
    //int hash2=vf2.hashCode();
    //assert hash1==hash2;
}
