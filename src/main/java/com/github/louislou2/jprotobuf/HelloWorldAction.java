package com.github.louislou2.jprotobuf;

import com.github.louislou2.jprotobuf.notifier.ChangeNotifier1;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HelloWorldAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        doChange1(e);
        Messages.showInfoMessage("Hello, World", "ToolsMenu");
        
    }
    public void doChange1(AnActionEvent e){
        Project project=e.getProject();
        System.out.println("@@@@@@@@@@@@@@@@@@"+project.getBasePath()+"@@@@@@@"+project.getName());
        ChangeNotifier1 publisher = project.getMessageBus()
                .syncPublisher(ChangeNotifier1.CHANGE_TOPIC_1);
        publisher.Changed1();
    }
}
