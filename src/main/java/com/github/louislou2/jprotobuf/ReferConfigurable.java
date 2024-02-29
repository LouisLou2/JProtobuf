//package com.github.louislou2.jprotobuf;
//
//import com.github.louislou2.jprotobuf.persistent.PluginSettingData;
//import com.intellij.openapi.options.Configurable;
//import javax.swing.JComponent;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//public class ReferConfigurable implements Configurable {
//    private JPanel myMainPanel;
//    private JTextField mySettingTextField;
//
//    // 提供给 PluginSettings 组件的引用
//    private final PluginSettingData settings;
//    
//    public ReferConfigurable() {
//        settings = PluginSettingData.getInstance();
//        initUi();
//    }
//    public void initUi(){
//        mySettingTextField = new JTextField();
//        myMainPanel = new JPanel();
//        myMainPanel.add(mySettingTextField);
//    }
//    @Override
//    public String getDisplayName() {
//        return "My Plugin Settings";   // 设置页面的名称
//    }
//
//    @Override
//    public JComponent createComponent() {
//        // 初始化你的设置 UI
//        return myMainPanel;
//    }
//
//    @Override
//    public boolean isModified() {
//        // 检查设置是否修改过，需要保存
//        assert settings.getState() != null;
//        return !mySettingTextField.getText().equals(settings.getState().protocPath);
//    }
//
//    @Override
//    public void apply() {
//        // 应用设置
//        assert settings.getState() != null;
//        settings.getState().protocPath = mySettingTextField.getText();
//    }
//
//    @Override
//    public void reset() {
//        // 从持久化的设置中重置 UI 元素的值
//        assert settings.getState() != null;
//        mySettingTextField.setText(settings.getState().protocPath);
//    }
//
//    @Override
//    public void disposeUIResources() {
//        // 清理方法，当设置页面关闭时调用
//    }
//
//    // ... 可能还有其他方法，比如 getHelpTopic(), getId(), enableSearch(), and so on.
//}