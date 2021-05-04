package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.view.cli.CLI;

public class LoadMultiPlayer extends ClientState {
    public LoadMultiPlayer() {
        CLI.getInstance().loadMultiPlayer();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line);

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json);

                        client.setModel(ClientParser.getInstance().getLocalModel(message));

                        CLI.getInstance().showLeaderCards();

                        client.setState(new InitDiscard());
                    }
                    default: {
                        CLI.getInstance().unexpected();
                    }
                }
            }
            default: {
                CLI.getInstance().unexpected();
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {

    }
}
