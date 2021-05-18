package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerManageWarehouse;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;

public class ManageWarehouse extends ClientState {
    private final ConcreteResourceSet obtained;
    private final ArrayList<ConcreteResourceSet> warehouse;
    private final ArrayList<Integer> playedLeaderCards;

    protected ManageWarehouse(ConcreteResourceSet obtained, ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        this.obtained = obtained;
        this.warehouse = warehouse;
        this.playedLeaderCards = playedLeaderCards;
        CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
        CLI.getInstance().manageWarehouse(obtained);
    }

    public static ClientState builder(ConcreteResourceSet obtained, ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerManageWarehouse(obtained, warehouse, playedLeaderCards);
        } else {
            return new ManageWarehouse(obtained, warehouse, playedLeaderCards);
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

    protected void handleConfirm(Client client) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", "confirmWarehouse");

        client.write(jsonObject.toString());
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int selection = Integer.parseInt(line);

            switch (selection) {
                case 1: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(AddToDepot.builder(obtained));
                    break;
                }
                case 2: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(RemoveFromDepot.builder(obtained));
                    break;
                }
                case 3: {
                    CLI.getInstance().showWarehouse(warehouse, playedLeaderCards);
                    client.setState(SwapFromDepots.builder(obtained));
                    break;
                }
                case 0: {
                    handleConfirm(client);
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
