package com.github.louislou2.jprotobuf.listener;

import com.github.louislou2.jprotobuf.notifier.ChangeNotifier1;
import com.intellij.openapi.project.Project;

public class Listener1 implements ChangeNotifier1 {
    private final Project project;
    public Listener1(Project project){
        this.project = project;
    }
    public Project getProject(){
        return project;
    }
    @Override
    public void Changed1() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@Changed() invoked@@@@@@@@@@@@@@@@@@@@");
    }
}
