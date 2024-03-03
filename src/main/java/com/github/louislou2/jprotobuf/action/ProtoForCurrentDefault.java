package com.github.louislou2.jprotobuf.action;

import com.github.louislou2.jprotobuf.service.TranslationManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class ProtoForCurrentDefault extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //Messages.showInfoMessage("wow", "uu");
        Project project = e.getProject();
        TranslationManager.writeProtoForCurrent_Default(project);
    }
}
