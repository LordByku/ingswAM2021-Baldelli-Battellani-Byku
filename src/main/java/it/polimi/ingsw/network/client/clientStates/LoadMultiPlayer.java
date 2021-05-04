package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.view.cli.CLI;
import jdk.vm.ci.meta.Local;

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

                        LocalModel model = ClientParser.getInstance().getLocalModel(message);

                        client.setModel(model);

                        CLI.getInstance().showLeaderCards(model.getPlayer(client.getNickname()).getBoard().getHandLeaderCards());

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
