package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import kotlinx.html.A;

import java.util.function.Function;

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
    public static String getDefineLocation(PsiClass aClass){
        return aClass.getNavigationElement().getContainingFile().getVirtualFile().getPath();
    }
    public static String getRelaCorProtoPath(PsiClass aClass, Function<PsiClass,String>getFileName){
        String theJavaPath=getDefineLocation(aClass);
        return getRelaCorProtoDir(theJavaPath)+'/'+getFileName.apply(aClass);
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
    public static PsiFile getPsiFileNowEditing(Project project){
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        assert editor != null;
        Document document = editor.getDocument();
        return PsiDocumentManager.getInstance(project).getPsiFile(document);
    }

    /**
     * 有时既需要PsiFile有需要path，请不要额外再次调用，因为获取到PsiFile自然可以通过paiFile.getVirtualFile().getPath()得到
     * @param project
     * @return
     */
    public static String getPathNowEditing(Project project){
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        // String url=editor.getVirtualFile().getUrl(); 结果形式: file://D:/SourceCode/a/User.java
        assert editor != null;
        return editor.getVirtualFile().getPath();//结果形式: D:/SourceCode/a/User.java
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
