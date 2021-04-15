package it.polimi.ingsw.devCards;

import java.util.Iterator;
import java.util.Stack;

public class DevCardDeck {
    private Stack<DevCard> devCardStack;

    public DevCardDeck() {
        devCardStack = new Stack<>();
    }

    public DevCard top(){
        return devCardStack.peek();
    }

    public void add(DevCard devCard) throws InvalidDevCardException, InvalidAddTopException {
        if(devCard == null) {
            throw new InvalidDevCardException();
        }
        if(topLevel() != devCard.getLevel().prev()) {
            throw new InvalidAddTopException();
        }
        devCardStack.push(devCard);
    }

    public CardTypeSet getCardTypeSet() {
        CardTypeSet cardTypeSet = new CardTypeSet();

        for (DevCard devCard : devCardStack) {
            cardTypeSet.add(devCard.getCardType());
        }

        return cardTypeSet;
    }

    public boolean isEmpty() {
       return devCardStack.isEmpty();
    }

    public int getPoints() {
        int points=0;
        for (DevCard devCard : devCardStack) {
            points += devCard.getPoints();
        }
        return points;
    }

    public CardLevel topLevel() {
        if(isEmpty()) {
            return null;
        }
        return top().getLevel();
    }
}
