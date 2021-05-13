package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;
import java.util.Arrays;

public class AddToDepot extends ClientState {
    private final ConcreteResourceSet obtained;

    public AddToDepot(ConcreteResourceSet obtained) {
        this.obtained = obtained;
        CLI.getInstance().addToDepot();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().addToDepot();
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

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);

        if(words.length < 2) {
            CLI.getInstance().addToDepot();
        } else {
            try {
                int depotIndex = Integer.parseInt(words[0]);
                ArrayList<ConcreteResourceSet> warehouse = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse();

                if(depotIndex < 0 || depotIndex >= warehouse.size()) {
                    CLI.getInstance().addToDepot();
                    return;
                }

                ConcreteResourceSet toAdd = ClientParser.getInstance().readUserResources(Arrays.copyOfRange(words, 1, words.length));

                if(obtained.contains(toAdd)) {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("command", "addToDepot");
                    jsonObject.addProperty("depotIndex", depotIndex);
                    jsonObject.add("set", ClientParser.getInstance().serialize(toAdd));

                    client.write(jsonObject.toString());
                } else {
                    CLI.getInstance().addToDepot();
                }
            } catch (NumberFormatException | InvalidResourceException | JsonSyntaxException e) {
                CLI.getInstance().addToDepot();
            }
        }
    }
}
