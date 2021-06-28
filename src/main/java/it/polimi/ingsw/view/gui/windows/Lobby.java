package it.polimi.ingsw.view.gui.windows;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Lobby extends GUIWindow {
    private JTable player;
    private JButton startGameButton;
    private JPanel panel;
    private JLabel errorLabel;
    private JTextField configFilename;
    private JButton loadCustomConfigFileButton;

    public Lobby(GUI gui, Client client, ArrayList<String> nicknames, String hostNickname) {
        super(gui, client);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Players:", ""}, 0);
        player.setModel(model);

        for (String nickname : nicknames) {
            if (nickname.equals(hostNickname)) {
                model.addRow(new String[]{nickname, "Host"});
            } else {
                model.addRow(new String[]{nickname, ""});
            }
        }

        AtomicReference<JsonObject> customConfig = new AtomicReference<>(null);

        startGameButton.setEnabled(LocalConfig.getInstance().isHost());

        startGameButton.addActionListener(new ButtonClickEvent((event) -> {
            errorLabel.setText(" ");
            if (customConfig.get() != null) {
                gui.bufferWrite(customConfig.get().toString());
            } else {
                gui.bufferWrite("");
            }
        }));

        loadCustomConfigFileButton.setEnabled(LocalConfig.getInstance().isHost());

        loadCustomConfigFileButton.addActionListener(new ButtonClickEvent((event) -> {
            try {
                String filename = configFilename.getText();
                File file = new File(filename);
                InputStreamReader reader = new FileReader(file);
                customConfig.set(new JsonParser().parse(reader).getAsJsonObject());
                errorLabel.setText("Config file loaded successfully");
            } catch (Exception e) {
                errorLabel.setText("An error occurred trying to load the file");
            }
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
        errorLabel.setText(message);
    }

    @Override
    public void connectionFailed(int timerDelay) {
        // TODO : i don't know what should happen here
    }

    @Override
    public void clearErrors() {
        // TODO : see connectionFailed
    }
}
