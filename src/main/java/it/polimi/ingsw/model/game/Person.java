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
     * isActivePlayer is a flag that tells whether it's currently
     * the turn of this Player
     */
    private boolean isActivePlayer;
    /**
     * board is the personal Board of the player
     */
    private Board board;
    /**
     * isHost checks whether this Person is the host of the lobby
     */
    private volatile boolean isHost;
    /**
     * isConnected indicates whether this Person is currently connected to the server
     */
    private volatile boolean isConnected;
    /**
     * initDiscarded indicates whether this Person has already discarded the initial leader cards
     */
    private volatile boolean initDiscarded;
    /**
     * initSelected indicates whether this Person has already selected their initial resources
     */
    private volatile boolean initSelected;
    /**
     * mainAction indicates whether this Person has already executed their main turn action
     */
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
        if (!Game.getInstance().allDisconnected()) {
            isActivePlayer = false;
            mainAction = false;
            Game.getInstance().handleTurnOrder();
        }
    }

    /**
     * getBoard returns a copy of the Board of this Player
     *
     * @return A copy of the Board of this Player
     */
    public Board getBoard() {
        if (board == null) {
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

    /**
     * isHost checks whether this Person is the host of the lobby
     * @return true iff this Person is the host of the lobby
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * isActivePlayer checks whether this Person is the active player
     * @return true iff this Person is the active player
     */
    public boolean isActivePlayer() {
        return isActivePlayer;
    }

    /**
     * isConnected checks whether this Person is connected
     * @return true iff this Person is connected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * disconnect disconnects this Person
     */
    public void disconnect() {
        isConnected = false;
    }

    /**
     * reconnect reconnects this Person
     */
    public void reconnect() {
        if (Game.getInstance().allDisconnected()) {
            isConnected = true;
            while (!isActivePlayer) {
                Person activePerson = Game.getInstance().getActivePerson();
                if(activePerson != null) {
                    activePerson.endTurn();
                }
            }
        } else {
            isConnected = true;
        }
    }

    /**
     * mainActionDone sets the mainAction flag to true
     */
    public void mainActionDone() {
        mainAction = true;
    }

    /**
     * mainAction returns the value of the mainAction flag
     * @return true iff this Person has already completed their main action in this turn
     */
    public boolean mainAction() {
        return mainAction;
    }

    /**
     * initDiscardDone sets the initDiscarded flag to true
     */
    public void initDiscardDone() {
        initDiscarded = true;
    }

    /**
     * initDiscarded returns the value of the initDiscarded flag
     * @return true iff this Person has already discarded the initial leader cards
     */
    public boolean initDiscarded() {
        return initDiscarded;
    }

    /**
     * initSelectDone sets the initSelected flag to true
     */
    public void initSelectDone() {
        initSelected = true;
    }

    /**
     * initSelected returns the value of the initSelected flag
     * @return true iff this Person has already selected their initial resources
     */
    public boolean initSelected() {
        return initSelected;
    }
}
