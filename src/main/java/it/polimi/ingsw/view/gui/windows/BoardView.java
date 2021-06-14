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
    private JPanel leaderCardsArea;
    private JPanel handLeaderCardsArea;
    private JPanel warehouse;
    private JPanel strongbox;
    private JPanel commands;
    private JButton startProductionButton;
    private JButton purchaseDevCardButton;
    private JButton collectResourcesButton;
    private JPanel bottomPanel;
    private JLabel errorLabel;

    public BoardView(GUI gui, Client client, String nickname) {
        super(gui, client);

        // TODO : load board's component according to nickname
        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new BoardToken(nickname));
        guiBottomPanel.loadBottomPanel();

        loadBoard();

        errorLabel.setForeground(Color.RED);
    }

    public void loadBoard() {
        loadFaithTrack(client);
        loadDevCardsArea(client);
        loadLeaderCardsArea(client);
        loadWarehouse(client);
        loadStrongbox(client);
        loadHandLeaderCardsArea(gui, client);
    }

    public void loadFaithTrack(Client client) {
        GUIFaithTrack guiFaithTrack = new GUIFaithTrack(client, faithTrack);
        guiFaithTrack.loadFaithTrack();
    }

    public void loadDevCardsArea(Client client) {
        GUIDevCardsArea guiDevCardsArea = new GUIDevCardsArea(client, devCardsArea);
        guiDevCardsArea.loadDevCardsArea();
    }

    public void loadLeaderCardsArea(Client client) {
        GUILeaderCardsArea guiLeaderCardsArea = new GUILeaderCardsArea(client, leaderCardsArea);
        guiLeaderCardsArea.loadLeaderCardsArea();
    }

    public void loadWarehouse(Client client) {
        GUIWarehouse guiWarehouse = new GUIWarehouse(client, warehouse);
        guiWarehouse.loadWarehouse();
    }

    public void loadStrongbox(Client client) {
        GUIStrongbox guiStrongbox = new GUIStrongbox(client, strongbox);
        guiStrongbox.loadStrongbox();
    }

    public void loadHandLeaderCardsArea(GUI gui, Client client) {
        GUIHandLeaderCards guiHandLeaderCards = new GUIHandLeaderCards(gui, client, handLeaderCardsArea);
        guiHandLeaderCards.loadHandLeaderCards();
    }

    public void initDiscard() {

    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {
        errorLabel.setText(message);
    }
}

