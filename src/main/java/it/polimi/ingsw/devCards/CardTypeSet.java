package it.polimi.ingsw.devCards;

import it.polimi.ingsw.leaderCards.LeaderCardRequirements;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * CardTypeset represents a set of CardTypes
 */

public class CardTypeSet implements LeaderCardRequirements {
    private HashMap<CardType,Integer> cardTypes;

    public CardTypeSet() {
        cardTypes = new HashMap<>();
    }

    public void add(CardType cardType) {
        int count = cardTypes.getOrDefault(cardType, 0);
        cardTypes.put(cardType, count + 1);
    }

    @Override
    public boolean isSatisfied(Board board) throws InvalidBoardException {
        ArrayList<DevCard> cards = board.getCards();

        for(Map.Entry<CardType, Integer> entry: cardTypes.entrySet()) {
            CardType cardType = entry.getKey();

            int satisfiedCount = 0;
            for(DevCard card: cards) {
                if(cardType.isSatisfied(card)) {
                    satisfiedCount++;
                }
            }

            if(satisfiedCount < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object clone() {
        try {
            CardTypeSet cloneSet = (CardTypeSet) super.clone();
            cloneSet.cardTypes = (HashMap<CardType, Integer>) cardTypes.clone();
            return cloneSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
