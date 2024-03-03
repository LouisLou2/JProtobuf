package com.github.louislou2.jprotobuf.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CustomLocationDialog extends DialogWrapper {
    private String currentJavaLocation;
    private JBTextField currentJavaLocationField;
    private JBTextField protoFileDirField;
    private JBTextField protifiedClassDirField;

    public CustomLocationDialog(@Nullable Project project,String currentJavaLocation) {
        super(project);
        // super(true) //use current window as parent
        this.currentJavaLocation=currentJavaLocation;
        setTitle("Enter the Custom Location");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new GridBagLayout());

        HyperlinkLabel linkLabel = new HyperlinkLabel();
        linkLabel.setHyperlinkText("Do not use this option if the Java class field contains custom classes ");
        linkLabel.addHyperlinkListener(e -> {
            Messages.showInfoMessage("When not working in a specific directory, the reference lookup will fail.", "Please Use Specific Mode");
        });
        LayoutHelper.addComponentForGridBag(dialogPanel, linkLabel, 0, 0, 2, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        
        var label1 = new JBLabel("Current Java Class Location:");
        LayoutHelper.addComponentForGridBag(dialogPanel, label1, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        currentJavaLocationField = new JBTextField(currentJavaLocation);
        currentJavaLocationField.setEditable(false);
        LayoutHelper.addComponentForGridBag(dialogPanel, currentJavaLocationField, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        
        var label2=new JBLabel("ProtoFile Location:");
        LayoutHelper.addComponentForGridBag(dialogPanel, label2, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        protoFileDirField = new JBTextField();
        LayoutHelper.addComponentForGridBag(dialogPanel, protoFileDirField, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        
        var label3=new JBLabel("Protified Java Class Location:");
        LayoutHelper.addComponentForGridBag(dialogPanel, label3, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        protifiedClassDirField = new JBTextField();
        LayoutHelper.addComponentForGridBag(dialogPanel, protifiedClassDirField, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);

        return dialogPanel;
    }
    
    public String getProtoFileDir() {
        return protoFileDirField.getText();
    }

    public String getProtifiedClassDir() {
        return protifiedClassDirField.getText();
    }
}