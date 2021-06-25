package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIUtil {
    public static JButton addButton(String text, JPanel outerPanel, ActionListener actionListener) {
        JPanel container = new JPanel(new GridBagLayout());

        JButton button = new JButton(text);
        button.setMargin(new Insets(1, 1, 1, 1));
        int width = 17 + 8 * text.length();
        button.setPreferredSize(new Dimension(width, 25));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addActionListener(actionListener);

        container.add(button);

        outerPanel.add(container);

        return button;
    }

    public static JButton addButton(String text, JPanel outerPanel, ActionListener actionListener, GridBagConstraints gbc) {
        JPanel container = new JPanel(new GridBagLayout());

        JButton button = new JButton(text);
        button.setMargin(new Insets(1, 1, 1, 1));
        int width = 17 + 8 * text.length();
        button.setPreferredSize(new Dimension(width, 25));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addActionListener(actionListener);

        container.add(button);

        outerPanel.add(container, gbc);

        return button;
    }

    public static JLabel addLabel(String text, JPanel outerPanel) {
        JPanel container = new JPanel(new GridBagLayout());

        JLabel label = new JLabel();
        label.setText(text);

        container.add(label);

        outerPanel.add(container);

        return label;
    }

    public static JCheckBox addCheckBox(String text, boolean isSelected, ButtonGroup checkBoxesButtonGroup, JPanel outerPanel, ActionListener actionListener) {
        JPanel container = new JPanel(new GridBagLayout());

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(text);
        checkBox.addActionListener(actionListener);

        checkBoxesButtonGroup.add(checkBox);
        checkBoxesButtonGroup.setSelected(checkBox.getModel(), isSelected);

        container.add(checkBox);

        outerPanel.add(container);

        return checkBox;
    }

    public static JRadioButton addRadioButton(String text, boolean isSelected, ButtonGroup buttonGroup,
                                              JPanel outerPanel, ActionListener actionListener) {
        JPanel container = new JPanel(new GridBagLayout());

        JRadioButton button = new JRadioButton();
        button.setText(text);
        button.setSelected(isSelected);
        button.addActionListener(actionListener);
        buttonGroup.add(button);

        container.add(button);

        outerPanel.add(container);

        return button;
    }
}
