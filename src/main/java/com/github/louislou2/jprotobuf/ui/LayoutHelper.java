package com.github.louislou2.jprotobuf.ui;

import java.awt.*;

public class LayoutHelper {
    private static final Insets insets = new Insets(0, 0, 0, 0);
    public static void addComponentForGridBag(Container container, Component component, int gridx, int gridy,
    int gridwidth, int gridheight, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0,
                anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }
}
