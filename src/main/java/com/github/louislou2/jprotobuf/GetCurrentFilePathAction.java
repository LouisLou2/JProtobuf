package com.github.louislou2.jprotobuf;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VfsUtil;

public class GetCurrentFilePathAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前工作的项目
        Project project = e.getData(CommonDataKeys.PROJECT);
        // 获取当前编辑的文件
        VirtualFile editingFile = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (project != null && editingFile != null) {
            // 获取项目的根目录
            VirtualFile projectBaseDir = ProjectUtil.guessProjectDir(project);
            // 获取当前编辑文件相对于项目根目录的相对路径
            assert projectBaseDir != null;
            String relativePath = VfsUtil.getRelativePath(editingFile, projectBaseDir);

            // 执行你需要的操作，例如输出到控制台
            System.out.println("Relative Path: " + relativePath);
        }
    }
}