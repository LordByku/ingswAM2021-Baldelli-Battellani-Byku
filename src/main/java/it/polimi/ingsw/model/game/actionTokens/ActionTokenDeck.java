package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.InvalidFaithTrackException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ActionTokenDeck is the deck of ActionTokens in single player games
 */
public class ActionTokenDeck {
    private static ActionToken flippedToken = null;
    /**
     * faithTrack is the Computer's Faith Track
     */
    private final FaithTrack faithTrack;
    /**
     * deck is the container of the ActionTokens
     */
    private final ArrayList<ActionToken> deck;

    /**
     * The constructor builds a new deck and initializes board to the given board
     *
     * @param faithTrack The FaithTrack of the Computer
     * @throws InvalidFaithTrackException faithTrack is null
     */
    public ActionTokenDeck(FaithTrack faithTrack) throws InvalidFaithTrackException {
        if (faithTrack == null) {
            throw new InvalidBoardException();
        }
        this.faithTrack = faithTrack;

        deck = new ArrayList<>();
        deck.add(ActionToken.DISCARDBLUE);
        deck.add(ActionToken.DISCARDGREEN);
        deck.add(ActionToken.DISCARDPURPLE);
        deck.add(ActionToken.DISCARDYELLOW);
        deck.add(ActionToken.ADVANCETWICE);
        deck.add(ActionToken.ADVANCETWICE);
        deck.add(ActionToken.ADVANCEONCEANDRESHUFFLE);

        Collections.shuffle(deck);
    }

    public static ActionToken getFlippedToken() {
        return flippedToken;
    }

    /**
     * removeTopToken removes the token on top of the deck
     */
    public void removeTopToken() {
        deck.remove(deck.size() - 1);
    }

    /**
     * flipFirstToken activates the effect of the token on top of the deck
     *
     * @return The new deck after applying the effect
     */
    public ActionTokenDeck flipFirstToken() {
        flippedToken = deck.get(deck.size() - 1);
        return flippedToken.flip(this);
    }

    /**
     * getBoard returns a copy of the board
     *
     * @return A copy of the board
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * size returns the size of this deck
     *
     * @return The number of ActionToken remaining in the deck
     */
    public int size() {
        return deck.size();
    }
}
