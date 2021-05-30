package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Welcome extends GUIWindow {
    private JPanel panel;
    private JButton playOfflineButton;
    private JButton playOnlineButton;
    private JTextField insertYourNicknameTextField;
    private JLabel connectionLabel;

    public Welcome(Client client) {
        playOnlineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: fix visibility
                connectionLabel.setVisible(false);
                client.handleUserMessage(insertYourNicknameTextField.getText());
                client.handleUserMessage("0");
            }
        });
        playOfflineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                connectionLabel.setVisible(false);
                client.handleUserMessage(insertYourNicknameTextField.getText());
                client.handleUserMessage("1");
            }
        });
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    public void startConnection() {
        connectionLabel.setText("Connecting to server...");
        connectionLabel.setVisible(true);
    }

    public void connectionFailed() {
        connectionLabel.setText("There was an error connecting to the server");
        connectionLabel.setVisible(true);
    }
}
