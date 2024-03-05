package com.github.louislou2.jprotobuf.service;

import com.github.louislou2.jprotobuf.constant.FileTypeEnum;
import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

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

    /**
     * 提供java文件路径，获取三统一规则下
     * 对应的.proto文件是否在对应文件夹
     * 或者对应的ProtoClass是否在对应文件夹
     * @param jpath
     * @param type
     * @return
     */
    public static boolean inSpecifiedDirWithJPath(String jpath, FileTypeEnum type){
        if(type==FileTypeEnum.POJO){
            return jpath.startsWith(pojoDir);
        }
        String className=jpath.substring(jpath.lastIndexOf('/')+1,jpath.lastIndexOf('.'));
        String filePath= switch (type){
            case PROTO -> protoDir+'/'+getRelaCorDir(jpath)+SpecRules.getProtoFileName.apply(className);
            case PROTO_CLASS -> protoClassDir+'/'+getRelaCorDir(jpath)+SpecRules.getProtoFileName.apply(className);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        return PathVirtualUtil.getVF(filePath)!=null;
    }

    /**
     * 
     * @param path 与type对应的path, 例如type=FileTYpeEnum.SELF, path应该形式类似：a/b/c/d/SelfDefined.java
     * @param type
     * @return
     */
    public static boolean inSpecifiedDir(String path, FileTypeEnum type){
        String prefix=switch (type){
            case POJO -> pojoDir;
            case PROTO -> protoDir;
            case PROTO_CLASS -> protoClassDir;
        };
        return path.startsWith(prefix);
    }
    public static String getDefineLocation(PsiClass aClass){
        return aClass.getNavigationElement().getContainingFile().getVirtualFile().getPath();
    }
    public static String getRelaCorProtoPath(String jpath){
        return getRelaCorDir(jpath)+'/'+SpecRules.getProtoFileName.apply(jpath);
    }
    public static String getRelaCorProtoPath(PsiClass aClass){
        String jpath=getDefineLocation(aClass);
        return getRelaCorDir(jpath)+'/'+SpecRules.getProtoFileName.apply(aClass.getName());
    }
    /**
     * 在三结构统一的规则下
     * 获取.proto文件所在的文件夹相对于protoDir的相对路径,前后都不带/
     * 例如它的位置是  protoDir/a/b/c/user.proto  则该方法会返回a/b/c
     * @param jpath
     * @return
     */
    public static String getRelaCorDir(String jpath){
        int lastSlashIndex=jpath.lastIndexOf('/');
        return jpath.substring(pojoDir.length()+1, lastSlashIndex);
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
}
