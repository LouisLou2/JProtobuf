package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class PathManager {
    public static VirtualFile pojoDirVir;
    public static VirtualFile protoDirVir;
    public static VirtualFile protoFileVir;
    static{
        ProjectSettingData prData = PluginSettingData.getInstance().getState().projectSettingData;
        pojoDirVir = PathVirtualUtil.getVF(prData.getPojoDir());
        protoDirVir = PathVirtualUtil.getVF(prData.getProtoDir());
        protoFileVir = PathVirtualUtil.getVF(prData.getProtoClassDir());
    }
    public static void updatePath(){
        ProjectSettingData prData = PluginSettingData.getInstance().getState().projectSettingData;
        pojoDirVir = PathVirtualUtil.getVF(prData.getPojoDir());
        protoDirVir = PathVirtualUtil.getVF(prData.getProtoDir());
        protoFileVir = PathVirtualUtil.getVF(prData.getProtoClassDir());
    }
    public static String getCorrespondingProtoDir(String theJavaPath){
        int lastSlashIndex=theJavaPath.lastIndexOf('/');
        return protoDirVir.getPath()+theJavaPath.substring(pojoDirVir.getPath().length(), lastSlashIndex);
        //不用replace，比对较多
        //return theDir.replace(pojoDirVir.getPath(),protoDirVir.getPath());
    }
    //public static void testPath(Project project) {
    //    String protocPath= PluginSettingData.getInstance().getState().applicationSettingData.getProtocPath();
    //    VirtualFile protocVir = PathVirtualUtil.getVF(protocPath);
    //    System.out.println(protocVir.getPath());
    //    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    //    VirtualFile nowEditingVir=FileDocumentManager.getInstance().getFile(editor.getDocument());
    //    System.out.println(nowEditingVir.getPath());
    //}
}
