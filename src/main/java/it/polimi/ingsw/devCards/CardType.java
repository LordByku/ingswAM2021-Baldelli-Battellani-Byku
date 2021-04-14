package it.polimi.ingsw.devCards;

import java.util.HashSet;

public class CardType {
    private CardColour colour;
    private HashSet<CardLevel> levelSet;

    public CardType(CardColour colour, HashSet<CardLevel> levelSet){
        this.colour=colour;
        this.levelSet=levelSet;
    }
}