package it.polimi.ingsw.editor.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ValidatableTextField {
    private final JPanel panel;
    private final JLabel errorLabel;
    private final JFormattedTextField textField;
    private final Predicate<Integer> validator;

    public ValidatableTextField(int text, Consumer<Integer> lambda, Predicate<Integer> validator) {
        panel = new JPanel();

        JPanel errorPanel = new JPanel(new GridBagLayout());
        JPanel textFieldPanel = new JPanel(new GridBagLayout());

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);

        textField = new JFormattedTextField();
        textField.setValue(text);
        Dimension dimension = textField.getPreferredSize();
        dimension.width = 30;
        textField.setPreferredSize(dimension);
        textField.getDocument().addDocumentListener(new TextFieldDocumentListener(textField, (value) -> {
            validate();
            lambda.accept(Integer.parseInt(value));
        }));

        errorPanel.add(errorLabel);
        textFieldPanel.add(textField);

        panel.add(errorPanel);
        panel.add(textFieldPanel);

        this.validator = validator;
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean validate() {
        boolean result;
        try {
            result = validator.test(Integer.parseInt(textField.getText()));
        } catch (NumberFormatException e) {
            result = false;
        }

        if (result) {
            errorLabel.setText(" ");
        } else {
            errorLabel.setText("!");
        }

        return result;
    }
}
