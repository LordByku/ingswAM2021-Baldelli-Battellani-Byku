package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.PlayedLeaderCardsArea;

import javax.swing.*;
import java.awt.*;

public class GUILeaderCardsArea implements LocalModelElementObserver {
    JPanel leaderCardsArea;
    Client client;
    private final PlayedLeaderCardsArea playedLeaderCards;

    public GUILeaderCardsArea(Client client, JPanel leaderCardsArea, String nickname) {
        this.client = client;
        this.leaderCardsArea = leaderCardsArea;
        playedLeaderCards = client.getModel().getPlayer(nickname).getBoard().getPlayedLeaderCards();

        playedLeaderCards.addObserver(this);
    }

    public void loadLeaderCardsArea() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        for (int playedLeaderCardID : playedLeaderCards.getLeaderCards()) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCardID);
            JPanel card = leaderCard.getLeaderCardImage(50);
            leaderCardsArea.add(card, c);
            c.gridy++;
        }
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            leaderCardsArea.removeAll();
            loadLeaderCardsArea();
            leaderCardsArea.revalidate();
            leaderCardsArea.repaint();
        });
    }

    @Override
    public void clean() {
        playedLeaderCards.removeObserver(this);
    }
}
