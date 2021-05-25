package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.utility.UserParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.localModel.Player;

import java.util.Arrays;

public class PurchaseWindow extends CommandWindow {
    private final ConcreteResourceSet[] warehouse;
    private final ConcreteResourceSet strongbox;

    public PurchaseWindow(Client client) {
        warehouse = new ConcreteResourceSet[LocalConfig.getInstance().getNumberOfDepots()];
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
                            client.write(buildCancelMessage().toString());
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
                        if (words.length == 3) {
                            int row = Integer.parseInt(words[0]);
                            int col = Integer.parseInt(words[1]);
                            int deckIndex = Integer.parseInt(words[2]);

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("row", row);
                            jsonObject.addProperty("column", col);
                            jsonObject.addProperty("deckIndex", deckIndex);
                            JsonObject message = buildCommandMessage("selection", jsonObject);
                            client.write(message.toString());
                            return;
                        }
                    }
                }
            } catch (NumberFormatException | JsonSyntaxException | InvalidResourceException e) {
            }
        }

        CLI.getInstance().renderWindow(client);
    }

    private void sendSpendResourcesCommand(Client client) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("warehouse", JsonUtil.getInstance().serialize(warehouse));
        jsonObject.add("strongbox", JsonUtil.getInstance().serialize(strongbox));
        JsonObject message = buildCommandMessage("spendResources", jsonObject);
        client.write(message.toString());
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        Purchase commandBuffer = (Purchase) self.getCommandBuffer();
        if (commandBuffer.getDeckIndex() == -1) {
            CLI.getInstance().purchaseDevCard();
        } else {
            int row = commandBuffer.getMarketRow();
            int col = commandBuffer.getMarketCol();
            int devCardID = client.getModel().getGameZone().getCardMarket().getDevCard(row, col);
            ConcreteResourceSet requirements = DevCardsParser.getInstance().getCard(devCardID).getReqResources();
            ChoiceResourceSet toSpend = new ChoiceResourceSet();
            toSpend.union(requirements);

            ConcreteResourceSet currentSelection = commandBuffer.getCurrentTotalToSpend();

            CLI.getInstance().showWarehouse(self.getBoard().getWarehouse(), self.getBoard().getPlayedLeaderCards());
            CLI.getInstance().showStrongbox(self.getBoard().getStrongBox());
            CLI.getInstance().spendResources(toSpend, currentSelection);
        }
    }
}
