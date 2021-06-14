package it.polimi.ingsw.view.gui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ButtonClickEvent extends MouseAdapter {
    private static boolean locked = false;
    private final Consumer<MouseEvent> lambda;
    private final boolean popupPriority;

    public ButtonClickEvent(Consumer<MouseEvent> lambda) {
        this(lambda, false);
    }

    public ButtonClickEvent(Consumer<MouseEvent> lambda, boolean popupPriority) {
        this.lambda = lambda;
        this.popupPriority = popupPriority;
        if (popupPriority) {
            locked = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!locked || popupPriority) {
            lambda.accept(e);
            if (popupPriority) {
                locked = false;
            }
        }
    }
}