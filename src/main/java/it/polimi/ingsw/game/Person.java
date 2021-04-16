package it.polimi.ingsw.game;

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
    boolean isActivePlayer;

    /**
     * The constructor creates a new Person given his/her nickname
     * @param nickname The nickname of the Person
     * @throws InvalidNicknameException nickname is null
     */
    public Person(String nickname) throws InvalidNicknameException {
        super();
        if(nickname == null) {
            throw new InvalidNicknameException();
        }
        this.nickname = nickname;
        isActivePlayer = false;
    }

    /**
     * getNickname returns the nickname of this Person
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
        isActivePlayer = true;
    }

    /**
     * endTurn sets the isActivePlayer flag to false and calls
     * the turn handler method in Game
     * @throws GameEndedException The Game already ended
     * @throws GameNotStartedException The Game did not start yet
     */
    @Override
    public void endTurn() throws GameEndedException, GameNotStartedException {
        isActivePlayer = false;
        Game.getInstance().handleTurnOrder();
    }
}
