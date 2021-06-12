package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class GUIHandLeaderCards {
    private final Client client;
    private final BlockingQueue<String> buffer;
    JPanel handLeaderCardsPanel;
    int numOfCardsToDiscard;
    ArrayList<Integer> handLeaderCards;

    public GUIHandLeaderCards(Client client, BlockingQueue<String> buffer, JPanel handLeaderCardsPanel) {
        this.client = client;
        this.buffer = buffer;
        this.handLeaderCardsPanel = handLeaderCardsPanel;
        numOfCardsToDiscard = LocalConfig.getInstance().getInitialDiscards();
        handLeaderCards = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards();
    }

    public void loadHandLeaderCards() {
        boolean doneInitDiscard = client.getModel().allInitDiscard();
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        for (int handLeaderCard : handLeaderCards) {
            JPanel cardPanel = new JPanel(new GridBagLayout());
            LeaderCard card = LeaderCardsParser.getInstance().getCard(handLeaderCard);
            JPanel cardImage = card.getLeaderCardImage(150);
            cardImage.setVisible(true);
            JButton discardButton = new JButton("discard");
            JButton playButton = new JButton("play");

            Player self = client.getModel().getPlayer(client.getNickname());
            discardButton.setEnabled(self.canDiscard(client.getModel()));
            playButton.setEnabled(self.canPlay(client.getModel()));

            c.gridy = 0;
            cardPanel.add(cardImage, c);
            c.gridy++;
            cardPanel.add(discardButton, c);
            c.gridy++;
            cardPanel.add(playButton, c);

            c.gridy = 0;
            c.insets = new Insets(0, 5, 0, 5);
            handLeaderCardsPanel.add(cardPanel, c);
            c.gridx++;
        }
    }
}
