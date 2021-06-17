package it.polimi.ingsw.view.gui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ButtonClickEvent extends MouseAdapter {
    private static volatile boolean locked = false;
    private final Consumer<MouseEvent> lambda;
    private final boolean priority;

    public ButtonClickEvent(Consumer<MouseEvent> lambda) {
        this(lambda, false);
    }

    public ButtonClickEvent(Consumer<MouseEvent> lambda, boolean priority) {
        this.lambda = lambda;
        this.priority = priority;
        if (priority) {
            locked = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!locked || priority) {
            lambda.accept(e);
            if(priority) {
                locked = false;
            }
        }
    }
}