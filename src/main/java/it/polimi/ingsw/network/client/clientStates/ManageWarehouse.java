package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;

public class ManageWarehouse extends ClientState {
    private final ConcreteResourceSet obtained;
    private final ArrayList<ConcreteResourceSet> warehouse;
    private final ArrayList<Integer> playedLeaderCards;

    public ManageWarehouse(ConcreteResourceSet obtained, ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        this.obtained = obtained;
        this.warehouse = warehouse;
        this.playedLeaderCards = playedLeaderCards;
        CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
        CLI.getInstance().manageWarehouse(obtained);
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                CLI.getInstance().manageWarehouse(obtained);
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.getModel().updateModel(message);

                        client.setState(new EndTurn());
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
        try {
            int selection = Integer.parseInt(line);

            switch (selection) {
                case 1: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(new AddToDepot(obtained));
                    break;
                }
                case 2: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(new RemoveFromDepot(obtained));
                    break;
                }
                case 3: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(new SwapFromDepots(obtained));
                    break;
                }
                case 0: {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("command", "confirmWarehouse");

                    client.write(jsonObject.toString());
                    break;
                }
                default: {
                    CLI.getInstance().manageWarehouse(obtained);
                }
            }
        } catch (NumberFormatException e) {
            CLI.getInstance().manageWarehouse(obtained);
        }
    }
}
