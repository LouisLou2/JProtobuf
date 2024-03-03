package com.github.louislou2.jprotobuf.debugger;

import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

public class FileVisual {
    public static void printContent(String path){
        // 获取你的 `.proto` 文件对应的VirtualFile对象，你可能需要知道文件的绝对路径或能够查询到它
        VirtualFile virtualFile = PathVirtualUtil.getVF(path);
        // 获取文件内容
        String content = null;
        try {
            content = VfsUtil.loadText(virtualFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 输出文件内容，此处仅示范，具体操作可能不同
        System.out.println(content);
    }
}
