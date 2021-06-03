package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.playerBoard.Board;

/**
 * Person represents human players
 */
public class Person extends Player {
    /**
     * nickname is the identifier of this Person
     */
    private final String nickname;
    /**
     * board is the personal Board of the player
     */
    private Board board;
    /**
     * isActivePlayer is a flag that tells whether it's currently
     * the turn of this Player
     */
    boolean isActivePlayer;
    /**
     * isHost checks whether this Person is the host of the lobby
     */
    private volatile boolean isHost;
    /**
     * isConnected indicates whether this Person is currently connected to the server
     */
    private volatile boolean isConnected;

    private volatile boolean initDiscarded;

    private volatile boolean initSelected;

    private volatile boolean mainAction;

    /**
     * The constructor creates a new Person given his/her nickname
     *
     * @param nickname The nickname of the Person
     * @throws InvalidNicknameException nickname is null
     */
    public Person(String nickname) throws InvalidNicknameException {
        super(PlayerType.PERSON);
        if (nickname == null) {
            throw new InvalidNicknameException();
        }
        this.nickname = nickname;
        isActivePlayer = false;
        board = null;
        isHost = false;
        isConnected = true;
        initDiscarded = false;
        initSelected = false;
        mainAction = false;
    }

    /**
     * getNickname returns the nickname of this Person
     *
     * @return The nickname of this Person
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * startTurn sets the isActivePlayer flag to true
     */
    @Override
    protected void startTurn() {
        if (isConnected) {
            isActivePlayer = true;
        } else {
            endTurn();
        }
    }

    /**
     * endTurn sets the isActivePlayer flag to false and calls
     * the turn handler method in Game
     *
     * @throws GameEndedException      The Game already ended
     * @throws GameNotStartedException The Game did not start yet
     */
    @Override
    public void endTurn() throws GameEndedException, GameNotStartedException {
        isActivePlayer = false;
        mainAction = false;
        if (!Game.getInstance().allDisconnected()) {
            Game.getInstance().handleTurnOrder();
        }
    }

    /**
     * getBoard returns a copy of the Board of this Player
     *
     * @return A copy of the Board of this Player
     */
    public Board getBoard() {
        if(board == null) {
            board = new Board();
        }
        return board.clone();
    }

    /**
     * setHost marks this Person as host of the lobby
     */
    protected void setHost() {
        isHost = true;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isActivePlayer() {
        return isActivePlayer;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() {
        isConnected = false;
    }

    public void reconnect() {
        if (Game.getInstance().allDisconnected()) {
            isConnected = true;
            while(!isActivePlayer) {
                Game.getInstance().handleTurnOrder();
            }
        } else {
            isConnected = true;
        }
    }

    public void mainActionDone() {
        mainAction = true;
    }

    public boolean mainAction() {
        return mainAction;
    }

    public void initDiscardDone() {
        initDiscarded = true;
    }

    public boolean initDiscarded() {
        return initDiscarded;
    }

    public void initSelectDone() {
        initSelected = true;
    }

    public boolean initSelected() {
        return initSelected;
    }
}
