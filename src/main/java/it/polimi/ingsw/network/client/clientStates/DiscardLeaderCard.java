package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerDiscardLeaderCard;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class DiscardLeaderCard extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    protected DiscardLeaderCard(Supplier<ClientState> returnStateSupplier) {
        CLI.getInstance().discardLeaderCard();
        this.returnStateSupplier = returnStateSupplier;
    }

    public static ClientState builder(Supplier<ClientState> returnStateSupplier) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerDiscardLeaderCard(returnStateSupplier);
        } else {
            return new DiscardLeaderCard(returnStateSupplier);
        }
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().discardLeaderCard();
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);
                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.getModel().updateModel(message);

                        CLI.getInstance().discardLeaderCardSuccess();
                        CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                        CLI.getInstance().discardLeaderCard();
                        break;
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

    protected void handleSelection(Client client, int selection) {
        JsonObject jsonObject = new JsonObject();
        JsonObject message = new JsonObject();
        message.addProperty("action", "discard");
        message.addProperty("leaderCard", selection);
        jsonObject.addProperty("command", "leaderCard");
        jsonObject.add("message", message);
        client.write(jsonObject.toString());
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
                    handleSelection(client, selection);
                } else {
                    CLI.getInstance().discardLeaderCard();
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().discardLeaderCard();
            }
        }
    }
}
