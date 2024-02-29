package com.github.louislou2.jprotobuf.properties;

import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectFocusDirectory {
    private static String PojoDir;
    private static String ProtoDir;
    private static String ProtoClassDir;
    private static VirtualFile PojoDirFile;
    private static VirtualFile ProtoDirFile;
    private static VirtualFile ProtoClassDirFile;
    public static void init(){
        
    }
}
