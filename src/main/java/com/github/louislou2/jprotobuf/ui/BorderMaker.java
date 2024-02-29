package com.github.louislou2.jprotobuf.ui;

import com.intellij.ui.JBColor;

import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class BorderMaker {
    public static TitledBorder titledBorderOnlyTop(String title) {
        // 创建仅在上方有1像素线的 MatteBorder
        Border matteBorder = new MatteBorder(1, 0, 0, 0, JBColor.GRAY);
        // 创建 TitledBorder，使用上面创建的 matteeBorder
        TitledBorder titledBorder = new TitledBorder(matteBorder, title);
        titledBorder.setTitleColor(JBColor.GRAY);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitlePosition(TitledBorder.CENTER);
        //titledBorder.setTitleFont(new Font("宋体", Font.PLAIN, 12));
        return titledBorder;
    }
}
