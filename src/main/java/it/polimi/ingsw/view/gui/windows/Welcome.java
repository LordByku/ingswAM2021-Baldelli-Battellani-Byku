package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.BlockingQueue;

public class Welcome extends GUIWindow {
    private JPanel panel;
    private JButton playOfflineButton;
    private JButton playOnlineButton;
    private JTextField insertYourNicknameTextField;
    private JLabel connectionLabel;
    private JLabel errorLabel;

    public Welcome(Client client, BlockingQueue<String> buffer) {
        playOnlineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                errorLabel.setText(" ");
                connectionLabel.setText(" ");
                try {
                    buffer.put(insertYourNicknameTextField.getText());
                    buffer.put("0");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        playOfflineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                errorLabel.setText(" ");
                connectionLabel.setText(" ");
                try {
                    buffer.put(insertYourNicknameTextField.getText());
                    buffer.put("1");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {
        connectionLabel.setText(" ");
        errorLabel.setText(message);
    }

    public void startConnection() {
        connectionLabel.setText("Connecting to server...");
    }

    public void connectionFailed() {
        connectionLabel.setText("There was an error connecting to the server");
    }
}
