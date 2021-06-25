package it.polimi.ingsw.view.gui.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ButtonClickEvent extends MouseAdapter implements ActionListener {
    private static volatile boolean locked = false;
    private final Consumer<AWTEvent> lambda;
    private final boolean priority;

    public ButtonClickEvent(Consumer<AWTEvent> lambda) {
        this(lambda, false);
    }

    public ButtonClickEvent(Consumer<AWTEvent> lambda, boolean priority) {
        this.lambda = lambda;
        this.priority = priority;
        if (priority) {
            locked = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        click(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        click(e);
    }

    protected void click(AWTEvent e) {
        if (!locked || priority) {
            lambda.accept(e);
            if (priority) {
                locked = false;
            }
        }
    }
}