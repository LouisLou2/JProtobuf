package com.github.louislou2.jprotobuf.persistent;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
@Service
@State(
    name = "JProtoPluginSettings",
    storages = @Storage("JProtoPluginSettings.xml")
)
public final class PluginSettingData implements PersistentStateComponent<PluginSettingData.State> {

    public static PluginSettingData getInstance() {
        return ApplicationManager.getApplication().getService(PluginSettingData.class);
    }

    public static class State {
        public ApplicationSettingData applicationSettingData = new ApplicationSettingData();
        public ProjectSettingData projectSettingData = new ProjectSettingData();
    }

    private State settingState = new State();

    @Override
    public State getState() {
        return settingState;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.settingState = state;
    }

    // ... 其他可能的方法和管理设置的逻辑
}