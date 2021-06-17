package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.utility.UserParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.localModel.Board;
import it.polimi.ingsw.view.localModel.Player;

import java.util.Arrays;
import java.util.HashMap;

public class ProductionWindow extends CommandWindow {
    private final ConcreteResourceSet[] warehouse;
    private final ConcreteResourceSet strongbox;

    public ProductionWindow(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        warehouse = new ConcreteResourceSet[self.getBoard().getWarehouse().getDepots().size()];
        for (int i = 0; i < warehouse.length; ++i) {
            warehouse[i] = new ConcreteResourceSet();
        }
        strongbox = new ConcreteResourceSet();
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);

        if (words.length > 0) {
            try {
                switch (words[0]) {
                    case "x": {
                        if (words.length == 1) {
                            JsonObject message = client.buildCancelMessage();
                            client.write(message.toString());
                            return;
                        }
                        break;
                    }
                    case "warehouse": {
                        if (words.length >= 2) {
                            int depotIndex = Integer.parseInt(words[1]);

                            if (depotIndex >= 0 && depotIndex < warehouse.length) {
                                ConcreteResourceSet toAdd = UserParser.getInstance().readUserResources(Arrays.copyOfRange(words, 2, words.length));
                                warehouse[depotIndex].union(toAdd);

                                sendSpendResourcesCommand(client);
                                return;
                            }
                        }
                        break;
                    }
                    case "strongbox": {
                        ConcreteResourceSet toAdd = UserParser.getInstance().readUserResources(Arrays.copyOfRange(words, 1, words.length));
                        strongbox.union(toAdd);

                        sendSpendResourcesCommand(client);
                        return;
                    }
                    default: {
                        try {
                            int[] toActivate = UserParser.getInstance().readIntArray(words);
                            JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(toActivate));
                            client.write(message.toString());
                            return;
                        } catch (NumberFormatException | JsonSyntaxException e) {
                            JsonArray jsonArray = new JsonArray();
                            for (String word : words) {
                                ConcreteResource concreteResource = UserParser.getInstance().readUserResource(word);
                                jsonArray.add(JsonUtil.getInstance().serialize(concreteResource));
                            }
                            JsonObject message = client.buildCommandMessage("choiceSelection", jsonArray);
                            client.write(message.toString());
                            return;
                        }
                    }
                }
            } catch (NumberFormatException | JsonSyntaxException | InvalidResourceException e) {
            }
        }

        CLI.renderGameWindow(client);
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        Production commandBuffer = (Production) self.getCommandBuffer();
        Board board = self.getBoard();
        HashMap<Integer, ProductionDetails> map = board.activeProductionDetails();
        if (commandBuffer.getProductionsToActivate() == null) {
            CLI.showWarehouse(board.getWarehouse().getDepots(), board.getPlayedLeaderCards());
            CLI.showStrongbox(board.getStrongBox());
            CLI.activateProductionSelection(map);
        } else {
            if (commandBuffer.getObtainedResources() == null) {
                ChoiceResourceSet toSpend = new ChoiceResourceSet();
                for (int toActivate : commandBuffer.getProductionsToActivate()) {
                    ProductionDetails productionDetails = map.get(toActivate);
                    toSpend.union(productionDetails.getInput().getResourceSet());
                }
                ConcreteResourceSet currentSelection = commandBuffer.getCurrentTotalToSpend();

                CLI.showWarehouse(self.getBoard().getWarehouse().getDepots(), self.getBoard().getPlayedLeaderCards());
                CLI.showStrongbox(self.getBoard().getStrongBox());
                CLI.spendResources(toSpend, currentSelection);
            } else {
                ChoiceResourceSet toObtain = new ChoiceResourceSet();
                for (int toActivate : commandBuffer.getProductionsToActivate()) {
                    ProductionDetails productionDetails = map.get(toActivate);
                    toObtain.union(productionDetails.getOutput().getResourceSet());
                }
                CLI.choiceResourceSelection(toObtain.getChoiceResources().size());
            }
        }
    }

    private void sendSpendResourcesCommand(Client client) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("warehouse", JsonUtil.getInstance().serialize(warehouse));
        jsonObject.add("strongbox", JsonUtil.getInstance().serialize(strongbox));
        JsonObject message = client.buildCommandMessage("spendResources", jsonObject);
        client.write(message.toString());
    }
}
