package com.github.louislou2.jprotobuf.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;

public class ProtoForCurrent_Group extends DefaultActionGroup {
    @Override
    public void update(AnActionEvent event) {
        // Enable/disable depending on whether a user is editing
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        // Take this opportunity to set an icon for the group.
        //event.getPresentation().setIcon(SdkIcons.Sdk_default_icon);
    }
}