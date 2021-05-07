package it.polimi.ingsw.network.server.serverStates;

import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;
import it.polimi.ingsw.network.server.serverStates.Lobby;
import it.polimi.ingsw.network.server.serverStates.ServerState;

public class AcceptNickname extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        String nickname = line;

        System.out.println("Received nickname " + nickname);
        try {
            clientHandler.setPlayer((Person) Game.getInstance().addPlayer(nickname));
            System.out.println("Accepted nickname " + nickname);
            clientHandler.broadcast("playerList", ServerParser.getInstance().getJsonPlayerList());
            clientHandler.setState(new Lobby());
        } catch (FullLobbyException e) {
            String errorMessage = "Lobby is already full";
            System.out.println("Rejected nickname " + nickname + ": " + errorMessage);
            clientHandler.fatalError(errorMessage);
        } catch (ExistingNicknameException e) {
            String errorMessage = "The nickname " + nickname + " has already been taken";
            System.out.println("Rejected nickname " + nickname + ": " + errorMessage);
            clientHandler.fatalError(errorMessage);
        } catch (InvalidNicknameException e) {
            String errorMessage = "The nickname is not valid";
            System.out.println("Rejected nickname " + nickname + ": " + errorMessage);
            clientHandler.fatalError(errorMessage);
        } catch (GameAlreadyStartedException e) {
            String errorMessage = "A game has already started";
            System.out.println("Rejected nickname " + nickname + ": " + errorMessage);
            clientHandler.fatalError(errorMessage);
        }
    }
}
