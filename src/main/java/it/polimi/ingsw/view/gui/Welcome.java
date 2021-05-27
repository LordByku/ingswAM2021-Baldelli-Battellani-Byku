package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Welcome {
    private JPanel panel;
    private JButton playOfflineButton;
    private JButton playOnlineButton;
    private JTextField insertYourNicknameTextField;

    public Welcome() {
        playOnlineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                insertYourNicknameTextField.setText("Suca Ema");
            }
        });
        playOfflineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                insertYourNicknameTextField.setText("Ema Suca");
            }
        });
    }

    public Container getPanel() {
        return panel;
    }
}
