package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;

public class LoadCards {
    private static LoadCards instance;

    public static LoadCards getInstance() {
        if (instance == null) {
            instance = new LoadCards();
        }
        return instance;
    }

    public void leaderCardWidth() {
        LeaderCard leaderCard;

        while ((leaderCard = LeaderCardsParser.getInstance().nextCard()) != null) {
            leaderCard.addCLISupport();
        }
    }

    public void devCardsWidth() {
        DevCard devCard;

        while ((devCard = DevCardsParser.getInstance().getNextCard()) != null) {
            devCard.addCLISupport();
        }
    }
}
