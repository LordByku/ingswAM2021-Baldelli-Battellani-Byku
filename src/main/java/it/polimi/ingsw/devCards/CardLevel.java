package it.polimi.ingsw.devCards;

/**
 * CardLevel represents all possible cards' levels
 */
public enum CardLevel {
    I,
    II,
    III;

    public CardLevel next(){
        if(this==I)
            return II;
        if(this==II)
            return III;
        return null;
    }

    public CardLevel prev(){
        if(this==II)
            return I;
        if(this==III)
            return II;
        return null;
    }
}
