package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;

public class InitResources extends ClientState {
    private final int maxSelections;
    private int selectionsLeft;
    private int currentDepotIndex;
    private final ConcreteResourceSet[] assignment;

    public InitResources(int maxSelections) {
        this.maxSelections = maxSelections;
        selectionsLeft = maxSelections;
        currentDepotIndex = 0;
        assignment = new ConcreteResourceSet[LocalConfig.getInstance().getNumberOfDepots()];
        for(int i = 0; i < assignment.length; ++i) {
            assignment[i] = new ConcreteResourceSet();
        }
        CLI.getInstance().initResources(selectionsLeft, currentDepotIndex);
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                client.setState(new InitResources(maxSelections));
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        client.getModel().updateModel(message);

                        boolean advanceState = true;
                        for(String nickname: LocalConfig.getInstance().getTurnOrder()) {
                            ArrayList<ConcreteResourceSet> warehouse = client.getModel().getPlayer(nickname).getBoard().getWarehouse();
                            ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                            for(ConcreteResourceSet depot: warehouse) {
                                concreteResourceSet.union(depot);
                            }
                            if(concreteResourceSet.size() != LocalConfig.getInstance().getInitialResources(nickname)) {
                                advanceState = false;
                            }
                        }

                        if(advanceState) {
                            if(client.getModel().getPlayer(client.getNickname()).hasInkwell()) {
                                client.setState(new StartTurn());
                            } else {
                                client.setState(new WaitTurn());
                            }
                        }

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
        if(words.length > selectionsLeft) {
            CLI.getInstance().initResources(selectionsLeft, currentDepotIndex);
        } else {
            try {
                ConcreteResourceSet selection = ClientParser.getInstance().readUserResources(words);

                assignment[currentDepotIndex].union(selection);

                selectionsLeft -= selection.size();

                if(selectionsLeft > 0) {
                    currentDepotIndex = (currentDepotIndex + 1) % assignment.length;

                    CLI.getInstance().initResources(selectionsLeft, currentDepotIndex);
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("command", "initResources");
                    jsonObject.add("value", ClientParser.getInstance().serialize(assignment));

                    System.out.println(jsonObject.toString());
                    client.write(jsonObject.toString());
                }
            } catch (JsonSyntaxException | InvalidResourceException e) {
                CLI.getInstance().initResources(selectionsLeft, currentDepotIndex);
            }
        }
    }
}
