package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.board.DevCardsArea.GUIDevCardsArea;
import it.polimi.ingsw.view.gui.board.*;
import it.polimi.ingsw.view.gui.board.faithTrack.GUIFaithTrack;
import it.polimi.ingsw.view.gui.windows.tokens.BoardToken;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoardView extends GUIWindow {
    private final String nickname;
    private JPanel panel;
    private JPanel faithTrack;
    private JPanel devCardsArea;
    private JPanel handLeaderCardsArea;
    private JPanel bottomPanel;
    private JLabel errorLabel;
    private JPanel leaderCardsArea;
    private JPanel commandsPanel;
    private JPanel warehouse;
    private JPanel strongbox;
    private GUIFaithTrack guiFaithTrack;
    private GUIWarehouse guiWarehouse;
    private GUIStrongbox guiStrongbox;
    private GUIDevCardsArea guiDevCardsArea;
    private GUIHandLeaderCards guiHandLeaderCards;
    private GUILeaderCardsArea guiLeaderCardsArea;
    private GUICommandsPanel guiCommandsPanel;

    public BoardView(GUI gui, Client client, String nickname) {
        super(gui, client);
        this.nickname = nickname;
        if (nickname.equals(client.getNickname())) {
            guiCommandsPanel = new GUICommandsPanel(gui, client, commandsPanel);
            guiCommandsPanel.loadCommandsPanel();
        }

        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new BoardToken(nickname));
        guiBottomPanel.loadBottomPanel();

        loadBoard(nickname);

        errorLabel.setForeground(Color.RED);
    }

    public void loadBoard(String nickname) {
        loadFaithTrack(client, nickname);
        loadDevCardsArea(client, nickname);
        loadLeaderCardsArea(client, nickname);
        loadWarehouse(client, nickname);
        loadStrongbox(client, nickname);
        loadHandLeaderCardsArea(client, nickname);
    }

    public void loadFaithTrack(Client client, String nickname) {
        guiFaithTrack = new GUIFaithTrack(client, faithTrack, nickname);
        guiFaithTrack.loadFaithTrack();
    }

    public void loadDevCardsArea(Client client, String nickname) {
        guiDevCardsArea = new GUIDevCardsArea(gui, client, devCardsArea, nickname);
        guiDevCardsArea.loadDevCardsArea();
    }

    public void loadLeaderCardsArea(Client client, String nickname) {
        guiLeaderCardsArea = new GUILeaderCardsArea(client, leaderCardsArea, nickname);
        guiLeaderCardsArea.loadLeaderCardsArea();
    }

    public void loadWarehouse(Client client, String nickname) {
        guiWarehouse = new GUIWarehouse(gui, client, warehouse, nickname);
        guiWarehouse.loadWarehouse();
    }

    public void loadStrongbox(Client client, String nickname) {
        guiStrongbox = new GUIStrongbox(gui, client, strongbox, nickname);
        guiStrongbox.loadStrongbox();
    }

    public void loadHandLeaderCardsArea(Client client, String nickname) {
        guiHandLeaderCards = new GUIHandLeaderCards(gui, client, handLeaderCardsArea, nickname);
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
        guiLeaderCardsArea.clean();
        if (nickname.equals(client.getNickname())) {
            guiCommandsPanel.clean();
        }
    }

    @Override
    public void onError(String message) {
        errorLabel.setText(message);
    }
}

