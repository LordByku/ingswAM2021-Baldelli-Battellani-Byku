package it.polimi.ingsw.devCards;

import java.util.Stack;

public class DevCardDeck {

    private Stack<DevCard> devCardStack;

    //private int points;

    public DevCardDeck() {
        devCardStack = new Stack<>();
    }

    public DevCard top(){
        return devCardStack.peek();
    }

    public void add(DevCard devCard) throws InvalidDevCardDeckException, InvalidAddTopException {
        if(devCard==null)
            throw new InvalidDevCardDeckException();
        else
            if(((this.top().getLevel()==CardLevel.I) && (devCard.getLevel()==CardLevel.II)) || ((this.top().getLevel()==CardLevel.II) && (devCard.getLevel()==CardLevel.III)))
                devCardStack.push(devCard);
            else throw new InvalidAddTopException();
    }

    //   public void remove() {  devCardStack.pop(); }

    public CardTypeSet getCardTypeSet (){

        CardTypeSet cardTypeSet = new CardTypeSet();

        for (DevCard devCard : devCardStack) {
            cardTypeSet.add(devCard.getCardType());
        }
        return cardTypeSet;
    }

    public boolean isEmpty(){
       return devCardStack.isEmpty();
    }


// public boolean isFull () {
//     return devCardStack.
// }

    public int getPoints() {
        int points=0;
        for (DevCard devCard : devCardStack) {
            points += devCard.getPoints();
        }
        return points;
    }
}
