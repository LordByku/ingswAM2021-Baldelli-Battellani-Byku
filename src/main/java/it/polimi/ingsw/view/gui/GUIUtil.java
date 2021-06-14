package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import java.awt.*;

public class GUIUtil {
    public static JButton addButton(String text, JPanel outerPanel, ButtonClickEvent clickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JButton button = new JButton(text);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setPreferredSize(new Dimension(25, 25));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addMouseListener(clickEvent);

        container.add(button);

        outerPanel.add(container);

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

    public static JCheckBox addCheckBox(String text, boolean isSelected, ButtonGroup checkBoxesButtonGroup, JPanel outerPanel, ButtonClickEvent buttonClickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(text);
        checkBox.addMouseListener(buttonClickEvent);

        checkBoxesButtonGroup.add(checkBox);
        checkBoxesButtonGroup.setSelected(checkBox.getModel(), isSelected);

        container.add(checkBox);

        outerPanel.add(container);

        return checkBox;
    }

    public static JRadioButton addRadioButton(String text, boolean isSelected, ButtonGroup buttonGroup,
                                              JPanel outerPanel, ButtonClickEvent buttonClickEvent) {
        JPanel container = new JPanel(new GridBagLayout());

        JRadioButton button = new JRadioButton();
        button.setText(text);
        button.setSelected(isSelected);
        button.addMouseListener(buttonClickEvent);
        buttonGroup.add(button);

        container.add(button);

        outerPanel.add(container);

        return button;
    }
}
