package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUILeaderCardsArea {
    JPanel leaderCardsArea;
    Client client;
    ArrayList<Integer> playedLeaderCards;

    public GUILeaderCardsArea(Client client, JPanel leaderCardsArea) {
        this.client = client;
        this.leaderCardsArea = leaderCardsArea;
        playedLeaderCards = client.getModel().getPlayer(client.getNickname()).getBoard().getPlayedLeaderCards();
    }

    public void loadLeaderCardsArea() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        for (int playedLeaderCardID : playedLeaderCards) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCardID);
            JPanel card = leaderCard.getLeaderCardImage(50);
            leaderCardsArea.add(card, c);
            c.gridy++;
        }
    }
}
