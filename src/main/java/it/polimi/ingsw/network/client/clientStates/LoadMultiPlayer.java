package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.HashSet;

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
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        LocalModel model = ClientParser.getInstance().getLocalModel(message);

                        client.setModel(model);

                        ArrayList<Integer> leaderCardsIDs = model.getPlayer(client.getNickname()).getBoard().getHandLeaderCards();

                        CLI.getInstance().showLeaderCards(leaderCardsIDs, new HashSet<>());

                        client.setState(new InitDiscard(leaderCardsIDs.size()));

                        break;
                    }
                    default: {
                        CLI.getInstance().unexpected();
                    }
                }

                break;
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
