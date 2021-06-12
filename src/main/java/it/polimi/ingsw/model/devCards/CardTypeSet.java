package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.leaderCards.LeaderCardRequirements;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.view.gui.images.leaderCard.CardTypeRequirementsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * CardTypeset represents a set of CardTypes
 */

public class CardTypeSet implements LeaderCardRequirements {
    /**
     * cardTypes represents a hash map of card types and his quantity
     */
    private TreeMap<CardColour, CardTypeDetails> cardTypes;

    /**
     * the constructor initializes a empty HashMap
     */
    public CardTypeSet() {
        cardTypes = new TreeMap<>();
    }

    public void add(CardTypeDetails cardTypeDetails) {
        cardTypes.put(cardTypeDetails.getCardColour(), cardTypeDetails);
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

        for (CardTypeDetails cardTypeDetails : cardTypes.values()) {
            int satisfiedCount = 0;
            for (DevCard card : cards) {
                if (cardTypeDetails.isSatisfied(card)) {
                    satisfiedCount++;
                }
            }

            if (satisfiedCount < cardTypeDetails.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object clone() {
        try {
            CardTypeSet cloneSet = (CardTypeSet) super.clone();
            cloneSet.cardTypes = (TreeMap<CardColour, CardTypeDetails>) cardTypes.clone();
            return cloneSet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCLIString() {
        StringBuilder result = new StringBuilder();
        for (CardTypeDetails cardTypeDetails : cardTypes.values()) {
            result.append(cardTypeDetails.getCLIString()).append(" ");
        }
        return result.toString();
    }

    @Override
    public JPanel getRequirementsPanel(int leaderCardWidth, int leaderCardHeight) {
        return new CardTypeRequirementsPanel(cardTypes, leaderCardWidth, leaderCardHeight).getPanel();
    }
}
