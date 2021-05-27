package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.Welcome;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InitWindow extends GUIWindow {
    public InitWindow(Client client, JFrame frame) {
        frame.setContentPane(new Welcome(client).getPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
