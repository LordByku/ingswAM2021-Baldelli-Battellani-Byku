package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;
import java.util.Stack;

public class DevCardDeck {
    /**
     * devCardStack represents the development card deck
     */
    private final Stack<DevCard> devCardStack;

    /**
     * DevCardDeck() initializes a empty stack
     */
    public DevCardDeck() {
        devCardStack = new Stack<>();
    }

    /**
     * top() shows the card on the top of a deck of development card
     * @return the top of a deck without removing the card
     */
    public DevCard top(){
        return devCardStack.peek();
    }

    /**
     * @param devCard the development card to be checked
     * @return true iff the development card che be add to a deck following the rules
     */
    public boolean canAdd(DevCard devCard) {
        return devCard.getLevel().prev() == topLevel();
    }

    /**
     * add represents the action to put a card on a top of a development card deck
     * @param devCard the card to be added
     * @throws InvalidDevCardException the devCard is null
     * @throws InvalidAddTopException the card cannot be added to the top
     */
    public void add(DevCard devCard) throws InvalidDevCardException, InvalidAddTopException {
        if(devCard == null) {
            throw new InvalidDevCardException();
        }
        if(!canAdd(devCard)) {
            throw new InvalidAddTopException();
        }
        devCardStack.push(devCard.clone());
    }

    /**
     * @return a list of the card inside the deck
     */
    public ArrayList<DevCard> getCards() {
        return new ArrayList<>(devCardStack);
    }

    public boolean isEmpty() {
       return devCardStack.isEmpty();
    }

    /**
     * @return the sums of the development card's points
     */
    public int getPoints() {
        int points=0;
        for (DevCard devCard : devCardStack) {
            points += devCard.getPoints();
        }
        return points;
    }

    /**
     * @return the card's level on the top
     */
    public CardLevel topLevel() {
        if(isEmpty()) {
            return null;
        }
        return top().getLevel();
    }

    public String getCLIString(){

        char c;
        int nl=0;
        StringBuilder result= new StringBuilder("");

        ArrayList<DevCard> devCards = this.getCards();

        if (devCards.size()==0) {
            return "So empty right here";
        }

        if(devCards.size()== 1) {
            result.append(devCards.get(0).getCLIString());
        }

        else if(devCards.size()== 2) {
            result.append(devCards.get(1).getCLIString()).append("\n");
            result.append(devCards.get(0).buildLastTwoRows(1));
        }

        else if (devCards.size()==3){
            result.append(devCards.get(2).getCLIString()).append("\n");
            result.append(devCards.get(1).buildLastTwoRows(1)).append("\n");
            result.append(devCards.get(0).buildLastTwoRows(2));
    /*
            for (int i=0;i<result.toString().length();i++)
                c=result.toString().charAt(i);

                if(c == '\n')
                    nl++;

                if(nl>=5 && nl<=7)
                    result.toString();



    */
        }

        return result.toString();
    }
}
