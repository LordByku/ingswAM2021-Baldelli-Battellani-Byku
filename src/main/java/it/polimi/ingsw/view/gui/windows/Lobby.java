package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Lobby extends GUIWindow {
    private JTable player;
    private JButton startGameButton;
    private JPanel panel;

    public Lobby(Client client, BlockingQueue<String> buffer, ArrayList<String> nicknames, String hostNickname) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Players:", ""}, 0);
        player.setModel(model);

        for (String nickname : nicknames) {
            if (nickname.equals(hostNickname)) {
                model.addRow(new String[]{nickname, "Host"});
            } else {
                model.addRow(new String[]{nickname, ""});
            }
        }

        startGameButton.setEnabled(LocalConfig.getInstance().isHost());

        startGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    buffer.put("");
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
    public GUIWindow refreshPlayerList(Client client, JFrame frame, BlockingQueue<String> buffer, ArrayList<String> nicknames, String hostNickname) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Players:", ""}, 0);
        player.setModel(model);

        for (String nickname : nicknames) {
            if (nickname.equals(hostNickname)) {
                model.addRow(new String[]{nickname, "Host"});
            } else {
                model.addRow(new String[]{nickname, ""});
            }
        }

        setActive(true, frame);

        startGameButton.setEnabled(LocalConfig.getInstance().isHost());

        return this;
    }

    @Override
    public void onError(String message) {

    }
}
