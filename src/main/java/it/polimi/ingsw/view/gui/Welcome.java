package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Welcome {
    private JPanel panel;
    private JButton playOfflineButton;
    private JButton playOnlineButton;
    private JTextField insertYourNicknameTextField;

    public Welcome(Client client) {
        playOnlineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                client.handleUserMessage(insertYourNicknameTextField.getText());
                client.handleUserMessage("0");
            }
        });
        playOfflineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                client.handleUserMessage(insertYourNicknameTextField.getText());
                client.handleUserMessage("1");
            }
        });
    }

    public Container getPanel() {
        return panel;
    }
}
