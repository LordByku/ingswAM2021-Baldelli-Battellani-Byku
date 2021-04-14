package it.polimi.ingsw.game;

import it.polimi.ingsw.game.actionTokens.ActionTokenDeck;

public class Computer extends Player {
    private ActionTokenDeck deck;

    public Computer() {
        super();
        deck = new ActionTokenDeck(getBoard());
    }

    @Override
    protected void startTurn() {
        deck = deck.flipFirstToken();
        endTurn();
    }

    @Override
    public void endTurn() {
        try {
            Game.getInstance().handleTurnOrder();
        } catch (GameNotStartedException | GameEndedException e) {
            e.printStackTrace();
        }
    }
}
