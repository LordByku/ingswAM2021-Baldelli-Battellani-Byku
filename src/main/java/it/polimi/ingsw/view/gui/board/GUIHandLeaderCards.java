package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIHandLeaderCards {
    JPanel handLeaderCardsPanel;
    Client client;
    int numOfCardsToDiscard;
    ArrayList<Integer> handLeaderCards;

    public GUIHandLeaderCards(Client client, JPanel handLeaderCardsPanel){
        this.client = client;
        this.handLeaderCardsPanel = handLeaderCardsPanel;
        numOfCardsToDiscard = LocalConfig.getInstance().getInitialDiscards();
        handLeaderCards = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards();
    }

    public void loadHandLeaderCards(){
        boolean doneInitDiscard = client.getModel().allInitDiscard();
        GridBagConstraints c = new GridBagConstraints();

        for (int i=0; i< handLeaderCards.size(); i++) {
            JPanel cardPanel = new JPanel(new GridBagLayout());
            LeaderCard card = LeaderCardsParser.getInstance().getCard(handLeaderCards.get(i));
            JPanel cardImage = card.getLeaderCardImage(50);
            cardImage.setVisible(true);
            JButton discardButton = new JButton("discard");
            JButton playButton = new JButton("play");
            if (!doneInitDiscard)
                playButton.setEnabled(false);

            c.gridy=0;
            cardPanel.add(cardImage,c);
            c.gridy++;
            cardPanel.add(discardButton, c);
            c.gridy++;
            cardPanel.add(playButton,c);

            c.gridy=0;
            c.insets = new Insets(0,5,0,5);
            handLeaderCardsPanel.add(cardPanel, c);
            c.gridx++;
        }
    }
}
