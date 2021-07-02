package it.polimi.ingsw.model.game;

/**
 * Player represents a player of the game
 */
public abstract class Player {
    /**
     * playerType indicates the type of Player
     */
    private final PlayerType playerType;

    /**
     * Default constructor
     * @param playerType the PlayerType of this Player
     */
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

    /**
     * getPlayerType returns the PlayerType of this Player
     * @return the PlayerType of this Player
     */
    public PlayerType getPlayerType() {
        return playerType;
    }
}
