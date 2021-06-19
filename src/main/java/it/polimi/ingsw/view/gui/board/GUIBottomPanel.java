package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
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

    public GUIBottomPanel(GUI gui, Client client, JPanel bottomPanel, WindowToken excludedToken) {
        this.gui = gui;
        this.client = client;
        this.bottomPanel = bottomPanel;
        this.excludedToken = excludedToken;

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    }

    private JButton addButton(String text, WindowToken windowToken) {
        if (excludedToken.equals(windowToken)) {
            return null;
        }

        return GUIUtil.addButton(text, bottomPanel, new ButtonClickEvent((event) -> {
            gui.switchGameWindow(windowToken);
        }));
    }

    public void loadBottomPanel() {
        ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();

        addButton("View Card Market", new CardMarketToken());
        addButton("View Marble Market", new MarbleMarketToken());
        for (String nickname : nicknames) {
            if (nickname.equals(client.getNickname())) {
                JButton button = addButton("View your board", new BoardToken(nickname));
                if (button != null) {
                    button.setForeground(Color.BLUE);
                }
            } else {
                addButton("View " + nickname + "'s board", new BoardToken(nickname));
            }
        }
    }
}
