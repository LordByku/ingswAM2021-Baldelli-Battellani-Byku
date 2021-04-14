package it.polimi.ingsw.devCards;

import java.util.HashMap;

/**
 * CardTypeset represents a set of CardTypes
 */

public class CardTypeSet{
    private HashMap<CardType,Integer> cardTypes;

    public HashMap<CardType, Integer> getCardTypes(){ return cardTypes; }
    // TODO: implements LeaderCardRequirements

    //@Override
    //public boolean isSatisfied(Board board) {
        //TODO:
        // method hasCardTypeSet in Board

        // return board.hasCardTypeSet(this.getCardTypes());
    //}

    public void add(CardType cardType) {
        int count = cardTypes.getOrDefault(cardType, 0);
        cardTypes.put(cardType, count + 1);
    }
}
