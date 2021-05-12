package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class PlayLeaderCard extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    public PlayLeaderCard(Supplier<ClientState> returnStateSupplier) {
        CLI.getInstance().playLeaderCard();
        this.returnStateSupplier = returnStateSupplier;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                client.setState(new PlayLeaderCard(returnStateSupplier));
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);
                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.getModel().updateModel(message);
                    }
                    default:{
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
        if(line.equals("x")){
            client.setState(returnStateSupplier.get());
        } else {
            try {
                int selection = Integer.parseInt(line);
                int numOfCards = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards().size();

                if(selection >= 0 && selection < numOfCards) {
                    JsonObject jsonObject = new JsonObject();
                    JsonObject message = new JsonObject();
                    message.addProperty("action", "play");
                    message.addProperty("leaderCard", selection);
                    jsonObject.addProperty("command", "leaderCard");
                    jsonObject.add("message", message);
                    client.write(jsonObject.toString());
                } else {
                    CLI.getInstance().discardLeaderCard();
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().discardLeaderCard();
            }
        }
    }
}
