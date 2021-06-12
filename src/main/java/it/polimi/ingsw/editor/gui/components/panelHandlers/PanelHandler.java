package it.polimi.ingsw.editor.gui.components.panelHandlers;

import javax.swing.*;

public abstract class PanelHandler {
    protected final JFrame frame;
    protected final JPanel panel;

    protected PanelHandler(JFrame frame, JPanel panel) {
        this.frame = frame;
        this.panel = panel;
    }

    public abstract void build();

    public abstract boolean validate();
}
