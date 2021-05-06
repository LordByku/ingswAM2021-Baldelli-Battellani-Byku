package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.LeaderCardArea;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * LeaderCardsDeck represents the deck of leader cards
 */
public class LeaderCardsDeck {
    /**
     * deck is the container of the LeaderCards
     */
    private final ArrayList<LeaderCard> deck;
    /**
     * assigned is the number of LeaderCards already assigned
     */
    private int assigned;

    /**
     * The constructor creates a new deck through the parser
     */
    public LeaderCardsDeck() {
        deck = new ArrayList<>();

        LeaderCard leaderCard;

        while((leaderCard = LeaderCardsParser.getInstance().nextCard()) != null) {
            deck.add(leaderCard);
        }

        Collections.shuffle(deck);

        assigned = 0;
    }

    /**
     * size returns the current size of the deck
     * @return The size of the deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * removeTop removes and returns the LeaderCard on top of this deck
     * @return The removed LeaderCard
     * @throws EmptyDeckException This deck is empty
     */
    public LeaderCard removeTop() throws EmptyDeckException {
        if(size() == 0) {
            throw new EmptyDeckException();
        }
        int lastIndex = size() - 1;
        LeaderCard leaderCard = deck.get(lastIndex);
        deck.remove(lastIndex);
        return leaderCard;
    }

    public void assignCards(LeaderCardArea leaderCardArea) {
        for(int i = assigned; i < assigned + 4; ++i) {
            leaderCardArea.addLeaderCard(deck.get(i));
        }

        assigned += 4;
    }
}
