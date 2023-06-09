package it.polimi.ingsw.model.game;

/**
 * ExistingNicknameException is thrown when a player with
 * the same nickname is already present in the lobby
 */
public class ExistingNicknameException extends Exception {
    public ExistingNicknameException() {
    }
}
