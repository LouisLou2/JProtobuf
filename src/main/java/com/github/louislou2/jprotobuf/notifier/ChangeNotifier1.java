package com.github.louislou2.jprotobuf.notifier;

import com.intellij.util.messages.Topic;


public interface ChangeNotifier1 {
    @Topic.ProjectLevel
    Topic<ChangeNotifier1> CHANGE_TOPIC_1 =
            Topic.create("customName:ChangeNotifier1", ChangeNotifier1.class);

    void Changed1();
}
