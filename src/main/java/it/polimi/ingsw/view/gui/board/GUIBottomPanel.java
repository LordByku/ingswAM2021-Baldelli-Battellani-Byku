package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.windows.tokens.BoardToken;
import it.polimi.ingsw.view.gui.windows.tokens.CardMarketToken;
import it.polimi.ingsw.view.gui.windows.tokens.MarbleMarketToken;
import it.polimi.ingsw.view.gui.windows.tokens.WindowToken;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIBottomPanel {
    private final GUI gui;
    private final Client client;
    private final JPanel bottomPanel;
    private final WindowToken excludedToken;
    private final GridBagConstraints gbc;

    public GUIBottomPanel(GUI gui, Client client, JPanel bottomPanel, WindowToken excludedToken) {
        this.gui = gui;
        this.client = client;
        this.bottomPanel = bottomPanel;
        this.excludedToken = excludedToken;

        bottomPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
    }

    private void addButton(String text, WindowToken windowToken) {
        if (excludedToken.equals(windowToken)) {
            return;
        }

        JPanel container = new JPanel();
        JButton button = new JButton(text);
        button.addMouseListener(new ButtonClickEvent((event) -> {
            gui.switchGameWindow(windowToken);
        }));
        container.add(button);
        bottomPanel.add(container, gbc);

        gbc.gridx++;
    }

    public void loadBottomPanel() {
        ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();
        int numOfPlayers = nicknames.size();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0 / (numOfPlayers + 1);
        gbc.insets = new Insets(10, 10, 10, 10);

        addButton("View Card Market", new CardMarketToken());
        addButton("View Marble Market", new MarbleMarketToken());
        for (String nickname : nicknames) {
            addButton("View " + nickname + "'s board", new BoardToken(nickname));
        }
    }
}
