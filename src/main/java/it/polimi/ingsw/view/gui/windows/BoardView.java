package it.polimi.ingsw.view.gui.windows;


import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.board.DevCardsArea.GUIDevCardsArea;
import it.polimi.ingsw.view.gui.board.GUIHandLeaderCards;
import it.polimi.ingsw.view.gui.board.GUILeaderCardsArea;
import it.polimi.ingsw.view.gui.board.GUIStrongbox;
import it.polimi.ingsw.view.gui.board.GUIWarehouse;
import it.polimi.ingsw.view.gui.board.faithTrack.GUIFaithTrack;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

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
    ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();

    public BoardView(Client client, BlockingQueue<String> buffer){

        nicknames.remove(client.getNickname());
        int numOfPlayers = nicknames.size();

        GridBagConstraints c = new GridBagConstraints();
        JPanel viewMarketsPanel = new JPanel();
        viewMarketsPanel.setVisible(true);
        //viewMarketsPanel.setBorder(new LineBorder(Color.BLACK));
        viewMarketsPanel.add(new JButton("View Markets"));
        c.gridx=0;
        c.gridy=0;
        c.weightx = 1.0/(numOfPlayers+1);
        c.insets = new Insets(10,10,10,10);
        bottomPanel.add(viewMarketsPanel, c);

        JPanel[] panels = new JPanel[3];

        for (int i=0; i<numOfPlayers; i++) {
            panels[i] = new JPanel();
            panels[i].setVisible(true);
            //panels[i].setBorder(new LineBorder(Color.BLACK));
            panels[i].add(new JButton("View " + nicknames.get(i) + " board"));
            c.gridx=i + 1;
            c.gridy=0;
            c.weightx = 1.0/(numOfPlayers+1);
            c.insets = new Insets(10,10,10,10);
            bottomPanel.add(panels[i], c);
        }

        loadBoard(client);

    }

    public void loadBoard(Client client){
        loadFaithTrack(client);
        loadDevCardsArea(client);
        loadLeaderCardsArea(client);
        loadWarehouse(client);
        loadStrongbox(client);
        loadHandLeaderCardsArea(client);
    }

    public void loadFaithTrack(Client client){
        GUIFaithTrack guiFaithTrack = new GUIFaithTrack(client, faithTrack);
        guiFaithTrack.loadFaithTrack();
    }

    public void loadDevCardsArea(Client client){
        GUIDevCardsArea guiDevCardsArea = new GUIDevCardsArea(client, devCardsArea);
        guiDevCardsArea.loadDevCardsArea();
    }

    public void loadLeaderCardsArea(Client client){
        GUILeaderCardsArea guiLeaderCardsArea = new GUILeaderCardsArea(client, leaderCardsArea);
        guiLeaderCardsArea.loadLeaderCardsArea();
    }

    public void loadWarehouse(Client client){
        GUIWarehouse guiWarehouse = new GUIWarehouse(client, warehouse);
        guiWarehouse.loadWarehouse();
    }

    public void loadStrongbox(Client client){
        GUIStrongbox guiStrongbox =  new GUIStrongbox(client, strongbox);
        guiStrongbox.loadStrongbox();
    }

    public void loadHandLeaderCardsArea(Client client){
        GUIHandLeaderCards guiHandLeaderCards = new GUIHandLeaderCards(client, handLeaderCardsArea);
        guiHandLeaderCards.loadHandLeaderCards();
    }

    public void initDiscard(){

    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {

    }
}

