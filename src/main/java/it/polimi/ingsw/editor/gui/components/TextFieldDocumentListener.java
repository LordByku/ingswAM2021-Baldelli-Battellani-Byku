package it.polimi.ingsw.editor.gui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class TextFieldDocumentListener implements DocumentListener {
    private final JFormattedTextField textField;
    private final Consumer<String> lambda;

    public TextFieldDocumentListener(JFormattedTextField textField, Consumer<String> lambda) {
        this.textField = textField;
        this.lambda = lambda;
    }

    private void update(DocumentEvent event) {
        try {
            lambda.accept(textField.getText());
        } catch (NumberFormatException e) {
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(e);
    }
}
