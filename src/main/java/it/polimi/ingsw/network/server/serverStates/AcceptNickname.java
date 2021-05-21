package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;
import it.polimi.ingsw.network.server.serverStates.Lobby;
import it.polimi.ingsw.network.server.serverStates.ServerState;

public class AcceptNickname extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject message = ServerParser.getInstance().parseLine(line).getAsJsonObject();
        String nickname = ServerParser.getInstance().getMessage(message).getAsString();

        System.out.println("Received nickname " + nickname);
        try {
            Person person = (Person) Game.getInstance().addPlayer(nickname);
            clientHandler.setPlayer(person);
            System.out.println("Accepted nickname " + nickname);
            if(Game.getInstance().hasGameStarted()) {
                clientHandler.sendGameState();

                JsonObject jsonObject = new JsonObject();
                if(Controller.getInstance().hasInitDiscarded(person)) {
                    if(Controller.getInstance().hasInitSelected(person)) {
                        jsonObject.addProperty("message", "startTurn");
                        clientHandler.ok("state", jsonObject);
                        clientHandler.setState(new StartTurn());
                    } else {
                        jsonObject.addProperty("message", "initResources");
                        clientHandler.ok("state", jsonObject);
                        clientHandler.setState(new InitResources());
                    }
                } else {
                    jsonObject.addProperty("message", "initDiscard");
                    clientHandler.ok("state", jsonObject);
                    clientHandler.setState(new InitDiscard());
                }
            } else {
                clientHandler.broadcast("playerList", ServerParser.getInstance().getJsonPlayerList());
                clientHandler.setState(new Lobby());
            }
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
