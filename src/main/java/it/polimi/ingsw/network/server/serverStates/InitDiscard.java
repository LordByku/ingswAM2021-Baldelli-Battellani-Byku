package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.ArrayList;
import java.util.function.Consumer;

public class InitDiscard extends ServerState {
    private static final ArrayList<ClientHandler> completed = new ArrayList<>();

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line);

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initDiscard")) {
            int[] indices = ServerParser.getInstance().parseIntArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initDiscard(clientHandler.getPerson(), indices)) {
                Consumer<GameStateSerializer> lambda;
                synchronized (completed) {
                    completed.add(clientHandler);
                    if(completed.size() == Game.getInstance().getNumberOfPlayers()) {
                        Controller.getInstance().handleInitialResources();
                        for(ClientHandler completedClientHandler: completed) {
                            completedClientHandler.setState(new InitResources());
                        }
                        lambda = (serializer) -> {
                            serializer.addHandLeaderCards(clientHandler.getPerson());
                            for(Player player: Game.getInstance().getPlayers()) {
                                serializer.addFaithTrack((Person) player);
                            }
                        };
                    } else {
                        lambda = (serializer) -> {
                            serializer.addHandLeaderCards(clientHandler.getPerson());
                        };
                    }
                }

                clientHandler.updateGameState(lambda);
            } else {
                clientHandler.error("Invalid choice");
            }
        } else {
            clientHandler.error("Invalid command");
        }
    }
}
