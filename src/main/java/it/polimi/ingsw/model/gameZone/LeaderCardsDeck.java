package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderCardsDeck {
    private final ArrayList<LeaderCard> deck;

    public LeaderCardsDeck() {
        deck = new ArrayList<>();

        LeaderCard leaderCard;

        while((leaderCard = LeaderCardsParser.getInstance().nextCard()) != null) {
            deck.add(leaderCard);
        }

        Collections.shuffle(deck);
    }

    public int size() {
        return deck.size();
    }

    public LeaderCard removeTop() {
        int lastIndex = deck.size() - 1;
        LeaderCard leaderCard = deck.get(lastIndex);
        deck.remove(lastIndex);
        return leaderCard;
    }
}
