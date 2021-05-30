package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Lobby extends GUIWindow {
    private JTable player;
    private JButton startGameButton;
    private JPanel panel;

    public Lobby(Client client, ArrayList<String> nicknames, String hostNickname) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Players:", ""}, 0);
        player.setModel(model);

        for(String nickname: nicknames) {
            if(nickname.equals(hostNickname)) {
                model.addRow(new String[]{nickname, "Host"});
            } else {
                model.addRow(new String[]{nickname, ""});
            }
        }
        startGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: start game
            }
        });
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }
}
