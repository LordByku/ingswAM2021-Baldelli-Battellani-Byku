package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSwapFromDepots;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;

public class SwapFromDepots extends ClientState {
    public SwapFromDepots(ConcreteResourceSet obtained) {
        CLI.getInstance().swapFromDepots();
    }

    public static ClientState builder(ConcreteResourceSet obtained) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerSwapFromDepots(obtained);
        } else {
            return new SwapFromDepots(obtained);
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
                CLI.getInstance().swapFromDepots();
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.getModel().updateModel(message);
                        break;
                    }
                    case "confirm": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        ConcreteResourceSet concreteResourceSet = ClientParser.getInstance().getConcreteResourceSet(message.getAsJsonObject("obtained"));

                        Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
                        client.setState(new ManageWarehouse(concreteResourceSet, playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards()));

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

    protected void handleSelection(Client client, int depotIndexA, int depotIndexB) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("command", "swapFromDepots");
        jsonObject.addProperty("depotIndexA", depotIndexA);
        jsonObject.addProperty("depotIndexB", depotIndexB);

        client.write(jsonObject.toString());
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);

        if(words.length != 2) {
            CLI.getInstance().swapFromDepots();
        } else {
            try {
                int depotIndexA = Integer.parseInt(words[0]), depotIndexB = Integer.parseInt(words[1]);
                ArrayList<ConcreteResourceSet> warehouse = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse();

                if(depotIndexA < 0 || depotIndexA >= warehouse.size() || depotIndexB < 0 || depotIndexB >= warehouse.size()) {
                    CLI.getInstance().swapFromDepots();
                    return;
                }

                handleSelection(client, depotIndexA, depotIndexB);
            } catch (NumberFormatException e) {
                CLI.getInstance().swapFromDepots();
            }
        }
    }
}
