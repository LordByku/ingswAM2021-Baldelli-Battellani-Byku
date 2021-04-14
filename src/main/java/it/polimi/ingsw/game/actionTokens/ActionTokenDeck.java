package it.polimi.ingsw.game.actionTokens;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;

import java.util.ArrayList;
import java.util.Collections;

public class ActionTokenDeck {
    private Board board;
    private ArrayList<ActionToken> deck;

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

    public void removeTopToken() {
        deck.remove(deck.size() - 1);
    }

    public ActionTokenDeck flipFirstToken() {
        return deck.get(deck.size() - 1).flip(this);
    }

    public Board getBoard() {
        return board.clone();
    }
}
