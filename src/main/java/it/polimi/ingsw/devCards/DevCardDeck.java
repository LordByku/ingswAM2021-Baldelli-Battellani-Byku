package it.polimi.ingsw.devCards;

import java.util.ArrayList;
import java.util.Stack;

public class DevCardDeck {
    private Stack<DevCard> devCardStack;

    public DevCardDeck() {
        devCardStack = new Stack<>();
    }

    public DevCard top(){
        return devCardStack.peek();
    }

    public boolean canAdd(DevCard devCard) {
        return devCard.getLevel().prev() == topLevel();
    }

    public void add(DevCard devCard) throws InvalidDevCardException, InvalidAddTopException {
        if(devCard == null) {
            throw new InvalidDevCardException();
        }
        if(!canAdd(devCard)) {
            throw new InvalidAddTopException();
        }
        devCardStack.push(devCard.clone());
    }

    public ArrayList<DevCard> getCards() {
        return new ArrayList<>(devCardStack);
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
