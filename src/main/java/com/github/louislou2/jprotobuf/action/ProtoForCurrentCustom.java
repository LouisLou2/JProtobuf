package com.github.louislou2.jprotobuf.action;

import com.github.louislou2.jprotobuf.service.PathManager;
import com.github.louislou2.jprotobuf.service.TranslationManager;
import com.github.louislou2.jprotobuf.ui.CustomLocationDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class ProtoForCurrentCustom extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project=e.getProject();
        //Messages.showInfoMessage("wow", "ew");
        var nowEditing = PathManager.getPsiFileNowEditing(project);
        String currentLocation=nowEditing.getVirtualFile().getPath();
        String protoFileDir = null;
        String protoClassDir= null;
        var queryDialog=new CustomLocationDialog(project,currentLocation);
        if(queryDialog.showAndGet()){
            // when user pressed ok
            protoFileDir=queryDialog.getProtoFileDir();
            protoClassDir=queryDialog.getProtifiedClassDir();
        }
        if(protoFileDir!=null&&protoClassDir!=null){
            TranslationManager.writeProto(project,nowEditing,protoFileDir,protoClassDir);
        }
    }
}
