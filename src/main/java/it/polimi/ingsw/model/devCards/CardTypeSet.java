package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.leaderCards.LeaderCardRequirements;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * CardTypeset represents a set of CardTypes
 */

public class CardTypeSet implements LeaderCardRequirements {
    /**
     * cardTypes represents a hash map of card types and his quantity
     */
    private HashMap<CardType,Integer> cardTypes;

    /**
     * the constructor initializes a empty HashMap
     */
    public CardTypeSet() {
        cardTypes = new HashMap<>();
    }

    /**
     * @param cardType the cardType added
     */
    public void add(CardType cardType) {
        int count = cardTypes.getOrDefault(cardType, 0);
        cardTypes.put(cardType, count + 1);
    }

    /**
     * @param board the board of the current player.
     * @return true iff the card types and their quantities on the board contains
     * the card types and their quantities expressed on the cardTypeSet
     * @throws InvalidBoardException if the board is null
     */
    @Override
    public boolean isSatisfied(Board board) throws InvalidBoardException {
        ArrayList<DevCard> cards = board.getDevelopmentCardArea().getCards();

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
