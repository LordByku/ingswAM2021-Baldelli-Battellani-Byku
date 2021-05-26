package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class EndGameWindow extends CLIWindow {
    private final JsonObject endGameMessage;

    public EndGameWindow(JsonObject endGameMessage) {
        this.endGameMessage = endGameMessage;
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        CLI.renderWindow(client);
    }

    @Override
    public void render(Client client) {
        CLI.endGame(endGameMessage);
    }
}
