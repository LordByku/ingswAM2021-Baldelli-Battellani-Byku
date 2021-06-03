package it.polimi.ingsw.editor.gui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ButtonClickEvent extends MouseAdapter {
    private final Consumer<MouseEvent> lambda;

    public ButtonClickEvent(Consumer<MouseEvent> lambda) {
        this.lambda = lambda;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lambda.accept(e);
    }
}