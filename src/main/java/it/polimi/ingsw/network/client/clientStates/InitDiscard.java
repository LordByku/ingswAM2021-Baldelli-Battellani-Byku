package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.HashSet;

public class InitDiscard extends ClientState {
    private final int maxSelection;

    public InitDiscard(int maxSelection) {
        CLI.getInstance().initDiscard();
        this.maxSelection = maxSelection;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line);

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                client.setState(new InitDiscard(maxSelection));
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "confirm": {
                        client.setState(new InitResources(LocalConfig.getInstance().getInitialResources(client.getNickname())));
                        break;
                    }
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        client.getModel().updateModel(message);

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
        String[] words = line.split("\\s+");
        if(words.length != 2) {
            client.setState(new InitDiscard(maxSelection));
        } else {
            try {
                int indexA = Integer.parseInt(words[0]), indexB = Integer.parseInt(words[1]);

                if(indexA < 0 || indexA >= maxSelection || indexB < 0 || indexB >= maxSelection || indexA == indexB) {
                    client.setState(new InitDiscard(maxSelection));
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("command", "initDiscard");

                    JsonArray jsonArray = new JsonArray();
                    jsonArray.add(indexA);
                    jsonArray.add(indexB);

                    jsonObject.add("value", jsonArray);

                    client.write(jsonObject.toString());
                }
            } catch(NumberFormatException e) {
                client.setState(new InitDiscard(maxSelection));
            }
        }
    }
}
