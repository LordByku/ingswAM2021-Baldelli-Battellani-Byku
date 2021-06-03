package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public abstract class PanelHandler {
    protected final JFrame frame;
    protected final JPanel panel;
    protected final GridBagConstraints gbc;

    protected PanelHandler(JFrame frame, JPanel panel) {
        this.frame = frame;
        this.panel = panel;
        gbc = new GridBagConstraints();
    }

    public abstract void build();

    protected void addTextField(int value, JPanel outerPanel, Consumer<Integer> lambda) {
        JPanel container = new JPanel(new GridBagLayout());

        JFormattedTextField textField = new JFormattedTextField();
        textField.setValue(value);
        Dimension dimension = textField.getPreferredSize();
        dimension.width = 30;
        textField.setPreferredSize(dimension);
        textField.getDocument().addDocumentListener(new TextFieldDocumentListener(textField, lambda));

        container.add(textField, gbc);

        outerPanel.add(container);
    }

    protected void addButton(String text, JPanel outerPanel, ButtonClickEvent clickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JButton button = new JButton(text);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setPreferredSize(new Dimension(25, 25));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addMouseListener(clickEvent);

        container.add(button, gbc);

        outerPanel.add(container);
    }

    protected void addLabel(String text, JPanel outerPanel) {
        JPanel container = new JPanel(new GridBagLayout());

        JLabel label = new JLabel();
        label.setText(text);

        container.add(label, gbc);

        outerPanel.add(container);
    }

    protected void addCheckBox(String text, boolean isSelected, JPanel outerPanel, ButtonClickEvent buttonClickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(text);
        checkBox.setSelected(isSelected);
        checkBox.addMouseListener(buttonClickEvent);

        container.add(checkBox, gbc);

        outerPanel.add(container);
    }

    protected void addRadioButton(String text, boolean isSelected, ButtonGroup buttonGroup,
                                  JPanel outerPanel, ButtonClickEvent buttonClickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JRadioButton button = new JRadioButton();
        button.setText(text);
        button.setSelected(isSelected);
        button.addMouseListener(buttonClickEvent);
        buttonGroup.add(button);

        container.add(button, gbc);

        outerPanel.add(container);
    }

    protected void clearListeners(AbstractButton button) {
        MouseListener[] listeners = button.getMouseListeners();
        for(MouseListener listener: listeners) {
            if(listener instanceof ButtonClickEvent) {
                button.removeMouseListener(listener);
            }
        }
    }
}
