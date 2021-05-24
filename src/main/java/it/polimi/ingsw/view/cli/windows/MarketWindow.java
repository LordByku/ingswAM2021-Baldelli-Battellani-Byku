package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.Market;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.leaderCards.WhiteConversionLeaderCard;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.localModel.Board;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.utility.UserParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.Arrays;

public class MarketWindow extends CommandWindow {
    public MarketWindow(Client client) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);

        if(words.length > 0) {
            try {
                switch (words[0]) {
                    case "x": {
                        if(words.length == 1) {
                            client.write(buildCancelMessage().toString());
                            return;
                        }
                        break;
                    }
                    case "row": {
                        if(words.length == 2) {
                            sendSelectionCommand(client, words, true);
                            return;
                        }
                        break;
                    }
                    case "col": {
                        if(words.length == 2) {
                            sendSelectionCommand(client, words, false);
                            return;
                        }
                        break;
                    }
                    case "add": {
                        if(words.length >= 2) {
                            sendDepotCommand(client, words, "addToDepot");
                            return;
                        }
                        break;
                    }
                    case "remove": {
                        if(words.length >= 2) {
                            sendDepotCommand(client, words, "removeFromDepot");
                            return;
                        }
                        break;
                    }
                    case "swap": {
                        if(words.length == 3) {
                            int depotIndexA = Integer.parseInt(words[1]);
                            int depotIndexB = Integer.parseInt(words[2]);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("depotIndexA", depotIndexA);
                            jsonObject.addProperty("depotIndexB", depotIndexB);
                            JsonObject message = buildCommandMessage("swapFromDepots", jsonObject);
                            client.write(message.toString());
                            return;
                        }
                        break;
                    }
                    case "confirm": {
                        if(words.length == 1) {
                            JsonObject message = buildCommandMessage("confirmWarehouse", JsonNull.INSTANCE);
                            client.write(message.toString());
                            return;
                        }
                        break;
                    }
                    default: {
                        JsonArray jsonArray = new JsonArray();
                        for (String word : words) {
                            ConcreteResource resource = UserParser.getInstance().readUserResource(word);
                            jsonArray.add(JsonUtil.getInstance().serialize(resource));
                        }
                        JsonObject message = buildCommandMessage("conversion", jsonArray);
                        client.write(message.toString());
                        return;
                    }
                }
            } catch (NumberFormatException | JsonSyntaxException | InvalidResourceException e) {}
        }
        render(client);
    }

    private void sendDepotCommand(Client client, String[] words, String command) {
        int depotIndex = Integer.parseInt(words[1]);
        ConcreteResourceSet toAdd = UserParser.getInstance().readUserResources(Arrays.copyOfRange(words, 2, words.length));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("depotIndex", depotIndex);
        jsonObject.add("resources", JsonUtil.getInstance().serialize(toAdd));
        JsonObject message = buildCommandMessage(command, jsonObject);
        client.write(message.toString());
    }

    private void sendSelectionCommand(Client client, String[] words, boolean rowColSel) {
        int index = Integer.parseInt(words[1]);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rowColSel", rowColSel);
        jsonObject.addProperty("index", index);
        JsonObject message = buildCommandMessage("selection", jsonObject);
        client.write(message.toString());
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        Market commandBuffer = (Market) self.getCommandBuffer();
        if(commandBuffer.getIndex() == -1) {
            CLI.getInstance().marbleMarketSelection();
        } else {
            Board board = self.getBoard();
            ChoiceResourceSet obtained = commandBuffer.getObtainedResources();
            if(!obtained.isConcrete()) {
                ChoiceSet choiceSet = new ChoiceSet();
                for(Integer leaderCardId: board.getPlayedLeaderCards()) {
                    LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardId);
                    if(leaderCard.isType(LeaderCardType.CONVERSION)) {
                        choiceSet.addChoice(((WhiteConversionLeaderCard) leaderCard).getType());
                    }
                }
                CLI.getInstance().whiteMarbleSelection(choiceSet, obtained.getChoiceResources().size());
            } else {
                CLI.getInstance().showWarehouse(board.getWarehouse(), board.getPlayedLeaderCards());
                CLI.getInstance().manageWarehouse(commandBuffer.getToDiscard());
            }
        }
    }
}
