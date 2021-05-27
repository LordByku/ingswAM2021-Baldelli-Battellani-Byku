package it.polimi.ingsw.view.gui.windows;

import javax.swing.*;

public abstract class GUIWindow {
    private final JPanel panel;

    protected GUIWindow(JPanel panel) {
        this.panel = panel;
    }

    public void setActive(boolean active, JFrame frame) {
        if(active) {
            frame.setContentPane(panel);
            frame.pack();
        }
        frame.setVisible(true);
    }
}

