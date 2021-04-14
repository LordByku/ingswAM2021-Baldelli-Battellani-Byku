package it.polimi.ingsw.game;

public class Person extends Player {
    private final String nickname;
    boolean isActivePlayer;

    public Person(String nickname) {
        this.nickname = nickname;
        isActivePlayer = false;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    protected void startTurn() {
        isActivePlayer = true;
    }

    @Override
    public void endTurn() throws GameEndedException, GameNotStartedException {
        isActivePlayer = false;
        Game.getInstance().handleTurnOrder();
    }
}
