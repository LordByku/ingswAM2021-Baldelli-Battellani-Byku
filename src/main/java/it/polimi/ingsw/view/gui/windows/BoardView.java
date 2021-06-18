package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.board.DevCardsArea.GUIDevCardsArea;
import it.polimi.ingsw.view.gui.board.*;
import it.polimi.ingsw.view.gui.board.faithTrack.GUIFaithTrack;
import it.polimi.ingsw.view.gui.windows.tokens.BoardToken;

import javax.swing.*;
import java.awt.*;

public class BoardView extends GUIWindow {
    private JPanel panel;
    private JPanel faithTrack;
    private JPanel devCardsArea;
    private JPanel handLeaderCardsArea;
    private JPanel bottomPanel;
    private JLabel errorLabel;
    private JPanel strongbox;
    private JPanel warehouse;
    private JPanel resourcesPanel;
    private JPanel leaderCardsArea;
    private JPanel commandsPanel;
    private JPanel resourcePanel;
    private GUIFaithTrack guiFaithTrack;
    private GUIWarehouse guiWarehouse;
    private GUIStrongbox guiStrongbox;
    private GUIDevCardsArea guiDevCardsArea;
    private GUIHandLeaderCards guiHandLeaderCards;
    private GUICommandsPanel guiCommandsPanel;

    public BoardView(GUI gui, Client client, String nickname) {
        super(gui, client);

        if(nickname.equals(client.getNickname())) {
            guiCommandsPanel = new GUICommandsPanel(gui, client, commandsPanel);
            guiCommandsPanel.loadCommandsPanel();
        }

        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new BoardToken(nickname));
        guiBottomPanel.loadBottomPanel();

        // TODO : load board's component according to nickname
        loadBoard(nickname);

        errorLabel.setForeground(Color.RED);
    }

    public void loadBoard(String nickname) {
        loadFaithTrack(client);
        loadDevCardsArea(client);
        loadLeaderCardsArea(client);
        loadWarehouse(client);
        loadStrongbox(client);
        loadHandLeaderCardsArea(client);
    }

    public void loadFaithTrack(Client client) {
        guiFaithTrack = new GUIFaithTrack(client, faithTrack);
        guiFaithTrack.loadFaithTrack();
    }

    public void loadDevCardsArea(Client client) {
        guiDevCardsArea = new GUIDevCardsArea(gui, client, devCardsArea);
        guiDevCardsArea.loadDevCardsArea();
    }

    public void loadLeaderCardsArea(Client client) {
        GUILeaderCardsArea guiLeaderCardsArea = new GUILeaderCardsArea(client, leaderCardsArea);
        guiLeaderCardsArea.loadLeaderCardsArea();
    }

    public void loadWarehouse(Client client) {
        guiWarehouse = new GUIWarehouse(gui, client, warehouse);
        guiWarehouse.loadWarehouse();
    }

    public void loadStrongbox(Client client) {
        guiStrongbox = new GUIStrongbox(client, strongbox);
        guiStrongbox.loadStrongbox();
    }

    public void loadHandLeaderCardsArea(Client client) {
        guiHandLeaderCards = new GUIHandLeaderCards(gui, client, handLeaderCardsArea);
        guiHandLeaderCards.loadHandLeaderCards();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    protected void clean() {
        guiFaithTrack.clean();
        guiWarehouse.clean();
        guiStrongbox.clean();
        guiDevCardsArea.clean();
        guiHandLeaderCards.clean();
        guiCommandsPanel.clean();
    }

    @Override
    public void onError(String message) {
        errorLabel.setText(message);
    }
}

