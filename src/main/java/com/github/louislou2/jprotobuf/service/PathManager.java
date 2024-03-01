package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

public class PathManager {
    public static VirtualFile pojoDirVir;
    public static VirtualFile protoDirVir;
    public static VirtualFile protoClassVir;
    public static String protocPath;
    public static String pojoDir;
    public static String protoDir;
    public static String protoClassDir;
    static{
        updatePath();
    }
    public static void updatePath(){
        ProjectSettingData prData = PluginSettingData.getInstance().getState().projectSettingData;
        protocPath=PluginSettingData.getInstance().getState().applicationSettingData.getProtocPath();
        pojoDir=prData.getPojoDir();
        protoDir=prData.getProtoDir();
        protoClassDir=prData.getProtoClassDir();
        pojoDirVir = PathVirtualUtil.getVF(pojoDir);
        protoDirVir = PathVirtualUtil.getVF(protoDir);
        protoClassVir = PathVirtualUtil.getVF(protoClassDir);
    }

    /**
     * 在三结构统一的规则下
     * 获取.proto文件所在的文件夹相对于protoDir的相对路径,前后都不带/
     * 例如它的位置是  protoDir/a/b/c/user.proto  则该方法会返回a/b/c
     * @param theJavaPath
     * @return
     */
    public static String getRelaCorProtoDir(String theJavaPath){
        int lastSlashIndex=theJavaPath.lastIndexOf('/');
        return theJavaPath.substring(pojoDir.length()+1, lastSlashIndex);
    }
    //public static String getCorProtoDirByRela(String relaPath){
    //    return protoDir+"/"+relaPath;
    //}
    //public static String ggetCorProtoDir(String theJavaPath){
    //    int lastSlashIndex=theJavaPath.lastIndexOf('/');
    //    return protoDir+theJavaPath.substring(pojoDir.length(), lastSlashIndex);
    //    //不用replace，比对较多
    //    //return theDir.replace(pojoDirVir.getPath(),protoDirVir.getPath());
    //}
    //public static String getCorProtoClassPath(String theJavaPath){
    //    int lastSlashIndex=theJavaPath.lastIndexOf('/');
    //    return protoClassDir+theJavaPath.substring(pojoDir.length(), lastSlashIndex);
    //}
    //public static void testPath(Project project) {
    //    String protocPath= PluginSettingData.getInstance().getState().applicationSettingData.getProtocPath();
    //    VirtualFile protocVir = PathVirtualUtil.getVF(protocPath);
    //    System.out.println(protocVir.getPath());
    //    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    //    VirtualFile nowEditingVir=FileDocumentManager.getInstance().getFile(editor.getDocument());
    //    System.out.println(nowEditingVir.getPath());
    //}
}
