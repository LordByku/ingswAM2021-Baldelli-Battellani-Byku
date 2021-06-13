package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.board.DevCardsArea.GUIDevCardsArea;
import it.polimi.ingsw.view.gui.board.GUIHandLeaderCards;
import it.polimi.ingsw.view.gui.board.GUILeaderCardsArea;
import it.polimi.ingsw.view.gui.board.GUIStrongbox;
import it.polimi.ingsw.view.gui.board.GUIWarehouse;
import it.polimi.ingsw.view.gui.board.faithTrack.GUIFaithTrack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class BoardView extends GUIWindow {
    private final ArrayList<String> nicknames;
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

    public BoardView(Client client, BlockingQueue<String> buffer) {
        nicknames = LocalConfig.getInstance().getTurnOrder();
        nicknames.remove(client.getNickname());
        int numOfPlayers = nicknames.size();
        JPanel jpanel;

        GridBagConstraints c = new GridBagConstraints();
        jpanel = new JPanel();
        jpanel.setVisible(true);
        //viewMarketsPanel.setBorder(new LineBorder(Color.BLACK));
        JButton marketView = new JButton("View Card Market");
        marketView.addMouseListener(new ButtonClickEvent((event) -> {
            // TODO : handle window switches better
            try {
                buffer.put("! cardmarket");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        jpanel.add(marketView);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0 / (numOfPlayers + 1);
        c.insets = new Insets(10, 10, 10, 10);
        bottomPanel.add(jpanel, c);

        jpanel = new JPanel();
        jpanel.setVisible(true);
        marketView = new JButton("View Marble Market");
        //TODO: add mouse listener
        jpanel.add(marketView);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0 / (numOfPlayers + 1);
        c.insets = new Insets(10, 10, 10, 10);
        bottomPanel.add(jpanel, c);

        JPanel[] panels = new JPanel[3];

        for (int i = 0; i < numOfPlayers; i++) {
            panels[i] = new JPanel();
            panels[i].setVisible(true);
            //panels[i].setBorder(new LineBorder(Color.BLACK));
            panels[i].add(new JButton("View " + nicknames.get(i) + " board"));
            c.gridx++;
            c.gridy = 0;
            c.weightx = 1.0 / (numOfPlayers + 1);
            c.insets = new Insets(10, 10, 10, 10);
            bottomPanel.add(panels[i], c);
        }

        loadBoard(client, buffer);

        errorLabel.setForeground(Color.RED);
    }

    public void loadBoard(Client client, BlockingQueue<String> buffer) {
        loadFaithTrack(client);
        loadDevCardsArea(client);
        loadLeaderCardsArea(client);
        loadWarehouse(client);
        loadStrongbox(client);
        loadHandLeaderCardsArea(client, buffer);
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

    public void loadHandLeaderCardsArea(Client client, BlockingQueue<String> buffer) {
        GUIHandLeaderCards guiHandLeaderCards = new GUIHandLeaderCards(client, buffer, handLeaderCardsArea);
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

