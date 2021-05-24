package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.utility.JsonUtil;

import java.util.function.Consumer;

public class AcceptNickname extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject message = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String nickname = message.get("nickname").getAsString();

        System.out.println("Received nickname " + nickname);
        try {
            Person currentPerson = (Person) Game.getInstance().addPlayer(nickname);
            clientHandler.setPlayer(currentPerson);
            System.out.println("Accepted nickname " + nickname);
            if(Game.getInstance().hasGameStarted()) {
                // handle reconnection here
                currentPerson.reconnect();
                clientHandler.ok("config", getConfigMessage());
                clientHandler.setState(new GameStarted());
                clientHandler.sendGameState();
                Consumer<GameStateSerializer> lambda = (serializer) -> {
                    serializer.addPlayerDetails(currentPerson);
                };
                clientHandler.updateGameState(lambda, clientHandler);
                clientHandler.sendAllCommandBuffers();
            } else {
                clientHandler.broadcast("playerList", GameStateSerializer.getJsonPlayerList());
                clientHandler.setState(new Lobby());
            }
        } catch (FullLobbyException e) {
            clientHandler.fatalError("Lobby is already full");
        } catch (ExistingNicknameException e) {
            clientHandler.fatalError("The nickname " + nickname + " has already been taken");
        } catch (InvalidNicknameException e) {
            clientHandler.fatalError("The nickname is not valid");
        } catch (GameAlreadyStartedException e) {
            clientHandler.fatalError("A game has already started");
        }
    }

    @Override
    public void forceTermination(ClientHandler clientHandler) {

    }
}
