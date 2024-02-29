package com.github.louislou2.jprotobuf.configurable;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.github.louislou2.jprotobuf.persistent.ApplicationSettingData;
import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.github.louislou2.jprotobuf.service.PathManager;
import com.github.louislou2.jprotobuf.ui.SettingPage;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MultiConfigurable implements Configurable {
    private SettingPage mySettingPage;
    private PluginSettingData settings;
    private final byte settingNum=4;
    private boolean[] settingChanged=new boolean[settingNum];
    /**
     * Returns the visible name of the configurable component.
     * Note, that this method must return the display name
     * that is equal to the display name declared in XML
     * to avoid unexpected errors.
     *
     * @return the visible name of the configurable component
     */
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "What About This";
    }

    /**
     * Creates a new Swing form that enables the user to configure the settings.
     * Usually this method is called on the EDT, so it should not take a long time.
     * <p>
     * Also, this place is designed to allocate resources (subscriptions/listeners etc.)
     *
     * @return new Swing form to show, or {@code null} if it cannot be created
     * @see #disposeUIResources
     */
    @Override
    public @Nullable JComponent createComponent() {
        settings = PluginSettingData.getInstance();
        assert settings.getState() != null;
        //这这里检查了一下数据持久化到底生效了没，这里是生效，正确的。
        //System.out.println(settings.getState().applicationSettingData.getProtocPath());
        mySettingPage = new SettingPage(settings.getState().applicationSettingData, settings.getState().projectSettingData);
        return mySettingPage.getPanel();
    }

    /**
     * Indicates whether the Swing form was modified or not.
     * This method is called very often, so it should not take a long time.
     *
     * @return {@code true} if the settings were modified, {@code false} otherwise
     */
    // 目前只有四个字段
    @Override
    public boolean isModified() {
        assert settings.getState() != null;
        ApplicationSettingData apData=settings.getState().applicationSettingData;
        ProjectSettingData prData=settings.getState().projectSettingData;
        // judge
        String protoPath=mySettingPage.protocPath.getText();
        String pojoDir=prData.getPojoDir();
        String protoDir=prData.getProtoDir();
        String protoClassDir=prData.getProtoClassDir();
        settingChanged[0]=!protoPath.equals(apData.getProtocPath());
        settingChanged[1]=!pojoDir.equals(mySettingPage.pojoDir.getText());
        settingChanged[2]=!protoDir.equals(mySettingPage.protoDir.getText());
        settingChanged[3]=!protoClassDir.equals(mySettingPage.protoClassDir.getText());
        for(int i=0;i<settingNum;i++){
            if(settingChanged[i]){
                return true;
            }
        }
        return false;
    }
    public void saveSettings(){
        // 根据settingChanged的值来判断是否需要保存
        assert settings.getState() != null;
        ApplicationSettingData apData=settings.getState().applicationSettingData;
        ProjectSettingData prData=settings.getState().projectSettingData;
        
        if(settingChanged[0]){
            apData.setProtocPath(mySettingPage.protocPath.getText());
        }
        if(settingChanged[1]){
            prData.setPojoDir(mySettingPage.pojoDir.getText());
        }
        if(settingChanged[2]){
            prData.setProtoDir(mySettingPage.protoDir.getText());
        }
        if(settingChanged[3]){
            prData.setProtoClassDir(mySettingPage.protoClassDir.getText());
        }
    }
    /**
     * Stores the settings from the Swing form to the configurable component.
     * This method is called on EDT upon user's request.
     *
     * @throws ConfigurationException if values cannot be applied
     */
    @Override
    public void apply() throws ConfigurationException {
        //每次设置state都会引起对于文件的读写，这是个慢操作，如果不新开线程，一定概率会出现Throwable:Slow operations are prohibited on EDT.
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 在这里执行耗时的任务
                saveSettings();
                PathManager.updatePath();
                return null;
            }
            @Override
            protected void done() {
                //System.out.println("@@@@@@@@@@@@@@done");
            }
        };
        worker.execute();
    }
    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingPage.getPreferredFocusedComponent();
    }
}
