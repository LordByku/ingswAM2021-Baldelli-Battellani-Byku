package it.polimi.ingsw.view.gui.windows;

import javax.swing.*;

public abstract class GUIWindow {
    public void setActive(boolean active, JFrame frame) {
        if(active) {
            frame.setContentPane(getPanel());
            frame.pack();
        }
        frame.setVisible(true);
    }

    protected abstract JPanel getPanel();
}

