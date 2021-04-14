package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.DevCardDeck;

import java.util.ArrayList;

public class DevelopmentCardArea implements Scoring {
    private ArrayList<DevCardDeck> decks;

    public DevelopmentCardArea() {
        decks = new ArrayList<>();
        decks.add(new DevCardDeck());
        decks.add(new DevCardDeck());
        decks.add(new DevCardDeck());
    }

    @Override
    public int getPoints() {
        int points = 0;
        for(DevCardDeck deck: decks) {
            points += deck.getPoints();
        }
        return points;
    }
}
