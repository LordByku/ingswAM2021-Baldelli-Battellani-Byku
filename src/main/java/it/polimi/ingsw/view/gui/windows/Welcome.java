package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;

public class Welcome extends GUIWindow {
    private JPanel panel;
    private JButton playOfflineButton;
    private JButton playOnlineButton;
    private JTextField insertYourNicknameTextField;
    private JLabel connectionLabel;
    private JLabel errorLabel;

    public Welcome(GUI gui, Client client) {
        super(gui, client);
        playOnlineButton.addActionListener(new ButtonClickEvent((event) -> {
            errorLabel.setText(" ");
            connectionLabel.setText(" ");
            gui.bufferWrite(insertYourNicknameTextField.getText());
            gui.bufferWrite("0");
        }));
        playOfflineButton.addActionListener(new ButtonClickEvent((event) -> {
            errorLabel.setText(" ");
            connectionLabel.setText(" ");
            gui.bufferWrite(insertYourNicknameTextField.getText());
            gui.bufferWrite("1");
        }));
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    protected void clean() {

    }

    @Override
    public GUIWindow onFatalError(JFrame frame, String message) {
        connectionLabel.setText(" ");
        errorLabel.setText(message);
        return this;
    }

    @Override
    public void onError(String message) {
        connectionLabel.setText(" ");
        errorLabel.setText(message);
    }

    @Override
    public void startConnection() {
        connectionLabel.setText("Connecting to server...");
    }

    @Override
    public void connectionFailed(int timerDelay) {
        connectionLabel.setText("There was an error connecting to the server");
    }

    @Override
    public void clearErrors() {
        connectionLabel.setText(" ");
        errorLabel.setText(" ");
    }
}
