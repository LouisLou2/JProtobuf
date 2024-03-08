package com.github.louislou2.jprotobuf.service;
import com.github.louislou2.jprotobuf.util.PathVirtualUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProtoGenerator {
    /**
     * 按照制定好的规则来书写.proto文件，
     * 即.proto文件关于protoDir的相对位置==.java关于pojoDir的相对位置
     * @return: 返回.proto文件相对于protoDir的相对位置
     * 例如：protoDir="a/b", protoFile="a/b/c/d/user.proto", 返回"c/d/user.proto"
     */
    public static String writeProtoFile_Default(PsiClass aclass, Project project,String javaFilePath){
        String relaProtoDir=PathManager.getRelaCorDir(javaFilePath);
        String corProtoPath=PathManager.protoDir+"/"+relaProtoDir;
        String protoFileContent = JavaParser.getProtoString(aclass);
        String protoFileName = SpecRules.getProtoFileName.apply(aclass.getName());
        PlainTextFileWriter.createFile(project, corProtoPath, protoFileName, protoFileContent);
        return relaProtoDir+"/"+protoFileName;
    }

    /**
     * 分析提供的classInfo 生成.proto文件内容，并写入targetFolder
     * @param aclass
     * @param project
     * @param targetFolder
     */
    public static void writeProtoFile(PsiClass aclass, Project project, String targetFolder) throws IllegalArgumentException{
        String protoFileContent = JavaParser.getProtoString(aclass);
        String protoFileName = SpecRules.getProtoFileName.apply(aclass.getName());
        PlainTextFileWriter.createFile(project, targetFolder, protoFileName, protoFileContent);
    }

    /**
     * 而protoc编译器自动就是这个原理，对于已存在的文件，它的策略就是更新内容，不存在才创建
     * @param searchingFolder
     * @param relaProtoFilePath
     * @param distDir
     */
    public static void writeProtoClassByProtoFile(String searchingFolder,String relaProtoFilePath,String distDir){
        PathVirtualUtil.createDirIfNonexistent(distDir);
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                String protocDir= PathManager.protocPath;
                // 构建命令和参数的列表
                // 不需要用-I 参数，因为protoc要找到import的文件
                List<String> command = Arrays.asList(
                        protocDir,
                        //"--experimental_allow_proto3_optional",
                        "-I=" + searchingFolder,
                        "--java_out=" + distDir,
                        relaProtoFilePath
                );
                // 使用ProcessBuilder运行命令
                ProcessBuilder builder = new ProcessBuilder(command);
                // 这里可以设置工作目录，如果需要的话
                // builder.directory(new File("/your/working/directory"));
                Process process = builder.start();
                
                // 现在没有考虑到日志，而此protoc没有控制台输出，所以下面的流操作先注释
                //// 读取命令的标准输出流
                //BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //String line;
                //while ((line = reader.readLine()) != null) {
                //    System.out.println(line);
                //}
                //
                //// 读取命令的标准错误流（如果需要的话）
                //BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                //while ((line = errorReader.readLine()) != null) {
                //    System.err.println(line);
                //}

                // 等待命令执行完成
                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    System.out.println("Success!");
                } else {
                    // 错误处理
                    System.out.println("Error with exit code: " + exitVal);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
