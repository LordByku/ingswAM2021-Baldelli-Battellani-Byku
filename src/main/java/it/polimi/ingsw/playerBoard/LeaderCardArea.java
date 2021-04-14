package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.leaderCards.LeaderCard;

import java.util.ArrayList;

/**
 * The area of the board containing the leaderCards currently active.
 */
public class LeaderCardArea implements Scoring {

    /**
     * leaderCards is an ArrayList containing the leaderCards currently active.
     */
    private ArrayList<LeaderCard> leaderCards;

    /**
     * Create an empty ArrayList of leaderCards
     */
    public LeaderCardArea(){
        leaderCards = new ArrayList<>();
    }

    /**
     * Adds a leaderCard to the Area.
     * @param leaderCard The leaderCards to be added.
     */
    public void addLeaderCard(LeaderCard leaderCard){
        leaderCards.add(leaderCard);
    }

    /**
     * Removes leaderCards from the Area.
     * @param leaderCard The leaderCards to be removed.
     */
    public void removeLeaderCard(LeaderCard leaderCard) {
        leaderCards.remove(leaderCard);
    }

    /**
     * Getter of leaderCards.
     * @return The leaderCards active on the Board.
     */
    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
