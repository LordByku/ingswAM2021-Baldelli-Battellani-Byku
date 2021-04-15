package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.*;

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

    public int IsLevelOnTop(CardLevel cardLevel){
        for (int i=0; i<3;i++) {
            if(decks.get(i).top().getLevel()==cardLevel)
                return i;
        }
        return -1;
    }

    public CardLevel getTopLevel(int deckIndex) throws InvalidDevCardDeckException {
        if(deckIndex < 0 || deckIndex >= decks.size()) {
            throw new InvalidDevCardDeckException();
        }

        return decks.get(deckIndex).topLevel();
    }

    public int numberOfDecks() {
        return decks.size();
    }

    public void addDevCard(DevCard devCard, int deckIndex) throws InvalidDevCardException, InvalidDevCardDeckException {
        if(deckIndex < 0 || deckIndex >= decks.size()) {
            throw new InvalidDevCardDeckException();
        }

        decks.get(deckIndex).add(devCard);
    }

    public ArrayList<DevCard> getCards() {
        ArrayList<DevCard> result = new ArrayList<>();
        for(DevCardDeck deck: decks) {
            result.addAll(deck.getCards());
        }
        return result;
    }
}
