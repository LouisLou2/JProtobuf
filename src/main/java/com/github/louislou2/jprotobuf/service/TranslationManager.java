package com.github.louislou2.jprotobuf.service;
import com.github.louislou2.jprotobuf.model.JavaClassInfo;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

public class TranslationManager {
    /**
     * 处理正在编辑的java class
     * 按照制定好的规则来书写.proto文件，
     * 即.proto文件关于protoDir的相对位置==.java关于pojoDir的相对位置==protoBuf化的.java相对于protoClassDir的位置
     */
    public static void writeProtoForCurrent_Default(Project project){
        // 获取正在编辑的文件的PsiFile实例
        PsiFile nowEditing=PathManager.getPsiFileNowEditing(project);
        // 获取此文件的路径
        String nowEditingPath=nowEditing.getVirtualFile().getPath();
        // 获取该文件的JavaClassInfo信息
        PsiClass aclass=PsiAnalyser.getPsiClass(nowEditing);
        
        // 获取生成文件的路径：使用在设置中设置管理目录，依照nowEditingPath 推算出结果文件应该存放的目录
        String relaThisProtoDir=PathManager.getRelaCorProtoDir(nowEditingPath);
        String protoTargetFolder=PathManager.protoDir+"/"+relaThisProtoDir; //.proto文件应该存放的目录
        
        String searchingFolder=PathManager.protoDir;//protoc命令的搜索目录
        String relaProtoFilePath=relaThisProtoDir+"/"+JavaParser.getProtoFileName(aclass);//.proto文件相对于searchingFolder的相对路径
        String distDir=PathManager.protoClassDir+"/"+relaThisProtoDir;// 目标Protofied Class的存放目录
        
        //开始书写文件
        ProtoGenerator.writeProtoFile(aclass,project,protoTargetFolder);
        ProtoGenerator.writeProtoClassByProtoFile(searchingFolder,relaProtoFilePath,distDir);
    }
    /**
     * 此方法过于自由，不建议使用，
     * 当原pojo涉及到不同java类的引用时，
     * 由于此方法并不在一个指定好的目录内活动，可能不能识别引用
     * 使用此方法请确保pojo的字段均为官方的java类型
     * @param project 该project
     * @param javaPsi 要解析的java文件的PsiFile实例
     * @param protoTarget 生成的.proto文件的存放目录
     * @param classDist 生成的ProtoBuf class的java文件存放目录
     */
    public static void writeProto(Project project,PsiFile javaPsi,String protoTarget,String classDist){
        PsiClass aclass=PsiAnalyser.getPsiClass(javaPsi);
        //开始书写文件
        ProtoGenerator.writeProtoFile(aclass,project,protoTarget);
        ProtoGenerator.writeProtoClassByProtoFile(protoTarget, JavaParser.getProtoFileName(aclass),classDist);
    }
}
