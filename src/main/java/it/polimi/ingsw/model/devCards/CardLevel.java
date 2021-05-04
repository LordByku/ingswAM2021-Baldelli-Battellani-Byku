package it.polimi.ingsw.model.devCards;

/**
 * CardLevel represents all possible cards' levels
 */
public enum CardLevel {
    I("1"),
    II("2"),
    III("3");

    private final String toString;

    CardLevel(String toString) {
        this.toString = toString;
    }

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

    @Override
    public String toString() {
        return toString;
    }
}
