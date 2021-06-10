package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
