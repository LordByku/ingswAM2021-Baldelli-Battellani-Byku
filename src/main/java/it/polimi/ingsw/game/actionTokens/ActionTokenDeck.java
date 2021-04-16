package it.polimi.ingsw.game.actionTokens;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ActionTokenDeck is the deck of ActionTokens in single player games
 */
public class ActionTokenDeck {
    /**
     * board is the Computer's Board
     */
    private Board board;
    /**
     * deck is the container of the ActionTokens
     */
    private ArrayList<ActionToken> deck;

    /**
     * The constructor builds a new deck and initializes board to the given board
     * @param board The Board of the Computer
     * @throws InvalidBoardException board is null
     */
    public ActionTokenDeck(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        this.board = board.clone();

        deck = new ArrayList<>();
        deck.add(new DiscardBlueToken());
        deck.add(new DiscardGreenToken());
        deck.add(new DiscardPurpleToken());
        deck.add(new DiscardYellowToken());
        deck.add(new AdvanceTwiceToken());
        deck.add(new AdvanceTwiceToken());
        deck.add(new AdvanceOnceAndReshuffleToken());

        Collections.shuffle(deck);
    }

    /**
     * removeTopToken removes the token on top of the deck
     */
    public void removeTopToken() {
        deck.remove(deck.size() - 1);
    }

    /**
     * flipFirstToken activates the effect of the token on top of the deck
     * @return The new deck after applying the effect
     */
    public ActionTokenDeck flipFirstToken() {
        return deck.get(deck.size() - 1).flip(this);
    }

    /**
     * getBoard returns a copy of the board
     * @return A copy of the board
     */
    public Board getBoard() {
        return board.clone();
    }

    /**
     * size returns the size of this deck
     * @return The number of ActionToken remaining in the deck
     */
    public int size() {
        return deck.size();
    }
}
