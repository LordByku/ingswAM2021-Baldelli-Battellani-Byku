package it.polimi.ingsw.model.game;

/**
 * Player represents a player of the game
 */
public abstract class Player {
    private final PlayerType playerType;

    protected Player(PlayerType playerType) {
        this.playerType = playerType;
    }

    /**
     * startTurn informs this Player that his/her turn started
     */
    protected abstract void startTurn();

    /**
     * endTurn is called by this Player to confirm that his/her turn ended
     *
     * @throws GameEndedException      The game already ended
     * @throws GameNotStartedException The game did not start yet
     */
    public abstract void endTurn() throws GameEndedException, GameNotStartedException;

    public PlayerType getPlayerType() {
        return playerType;
    }
}
