package com.github.louislou2.jprotobuf.ui;

import com.github.louislou2.jprotobuf.persistent.ApplicationSettingData;
import com.github.louislou2.jprotobuf.persistent.ProjectSettingData;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SettingPage {
    public JBPanel wholePane;
    public JBLabel pojoDirLabel;
    public TextFieldWithBrowseButton pojoDir;
    public JBLabel protoDirLabel;
    public TextFieldWithBrowseButton protoDir;
    public JBLabel protoClassDirLabel;
    public TextFieldWithBrowseButton protoClassDir;
    public JBList projectList;
    public JBPanel listPane;
    public JBTextField textField1;
    public TextFieldWithBrowseButton protocPath;
    public JBTextField textField3;
    public JBLabel label1;
    public JBLabel label2;
    public JBLabel label3;
    public FileChooserDescriptor onlyFileDescriptor;
    public FileChooserDescriptor onlyDirectoryDescriptor;
    private ApplicationSettingData apData;
    private ProjectSettingData prData;
    public SettingPage(ApplicationSettingData apData, ProjectSettingData prData) {
        this.apData = apData;
        this.prData = prData;
        initFileChooserDescriptor();
        // initUi会使用到FileChooserDescriptor，apData，prData，所以要在initUi之前初始化
        initUi();
    }
    public JBPanel getPanel() {
        return wholePane;
    }
    public TextFieldWithBrowseButton getPreferredFocusedComponent() {
        return protocPath;
    }
    private void initFileChooserDescriptor() {
        onlyFileDescriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        onlyDirectoryDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
    }
    private void initUi(){
        wholePane = new JBPanel();
        wholePane.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        wholePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        wholePane.add(panel1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderMaker.titledBorderOnlyTop("Basic"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label1 = new JBLabel();
        label1.setText("Label1:Unused:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JBTextField();
        panel2.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label2 = new JBLabel();
        label2.setText("ProtocPath:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        //
        protocPath = new TextFieldWithBrowseButton();
        //textField2.addBrowseFolderListener(
        //        new TextBrowseFolderListener(fileChooserDescriptor, getP()) {
        //            @Override
        //            public void actionPerformed(ActionEvent e) {
        //                System.out.println("@@@@@@@@@@invoked");
        //                // 这里可以额外处理动作事件
        //                super.actionPerformed(e);
        //            }
        //        }
        //);
        protocPath.addBrowseFolderListener(
                "Select File11", // Dialog Title
                "Choose a file to add11", // Dialog description
                ProjectManager.getInstance().getDefaultProject(), // 使用当前项目作为上下文
                onlyFileDescriptor
        );
        panel3.add(protocPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label3 = new JBLabel();
        label3.setText("Label3:Unused");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField3 = new JBTextField();
        panel4.add(textField3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        wholePane.add(panel5, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderMaker.titledBorderOnlyTop("Projects"));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderMaker.titledBorderOnlyTop("For Each Project"));
        pojoDirLabel = new JBLabel();
        pojoDirLabel.setText("PojoDir:");
        panel6.add(pojoDirLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pojoDir = new TextFieldWithBrowseButton();
        pojoDir.addBrowseFolderListener(
                "Select Directory22", // Dialog Title
                "Choose a directory to add22", // Dialog description
                ProjectManager.getInstance().getDefaultProject(), // 使用当前项目作为上下文
                onlyDirectoryDescriptor
        );
        panel6.add(pojoDir, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        protoDirLabel = new JBLabel();
        protoDirLabel.setText("ProtoDir:");
        panel6.add(protoDirLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        protoDir = new TextFieldWithBrowseButton();
        protoDir.addBrowseFolderListener(
                "Select Directory33", // Dialog Title
                "Choose a directory to add33", // Dialog description
                ProjectManager.getInstance().getDefaultProject(), // 使用当前项目作为上下文
                onlyDirectoryDescriptor
        );
        panel6.add(protoDir, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        protoClassDirLabel = new JBLabel();
        protoClassDirLabel.setText("ProtoClassDir:");
        panel6.add(protoClassDirLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        protoClassDir = new TextFieldWithBrowseButton();
        protoClassDir.addBrowseFolderListener(
                "Select Directory44", // Dialog Title
                "Choose a directory to add44", // Dialog description
                ProjectManager.getInstance().getDefaultProject(), // 使用当前项目作为上下文
                onlyDirectoryDescriptor
        );
        panel6.add(protoClassDir, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel6.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listPane = new JBPanel();
        listPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(listPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        listPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-8552835)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        projectList = new JBList();
        listPane.add(projectList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        // initiate data
        textField1.setText("unused");
        textField3.setText("unused");
        protocPath.setText(apData.getProtocPath());
        pojoDir.setText(prData.getPojoDir());
        protoDir.setText(prData.getProtoDir());
        protoClassDir.setText(prData.getProtoClassDir());
    }
}
