package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.leaderCards.WhiteConversionLeaderCard;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.network.client.localModel.MarbleMarket;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

public class CollectResources extends ClientState {
    private boolean rowColSel;
    private int index;

    public CollectResources() {
        CLI.getInstance().marbleMarketSelection();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        MarbleMarket marbleMarket = client.getModel().getGameZone().getMarbleMarket();

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().marbleMarket(marbleMarket);
                CLI.getInstance().marbleMarketSelection();
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "confirm": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        if(message.has("choices")) {
                            ChoiceSet choiceSet = new ChoiceSet();
                            for(Integer leaderCardId: client.getModel().getPlayer(client.getNickname()).getBoard().getPlayedLeaderCards()) {
                                LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardId);
                                if(leaderCard.isType(LeaderCardType.CONVERSION)) {
                                    choiceSet.addChoice(((WhiteConversionLeaderCard) leaderCard).getType());
                                }
                            }

                            client.setState(new WhiteMarbleSelection(choiceSet, message.get("choices").getAsInt()));
                        } else if(message.has("obtained")) {
                            Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
                            ConcreteResourceSet obtained = ClientParser.getInstance().getConcreteResourceSet(message.getAsJsonObject("obtained"));
                            client.setState(new ManageWarehouse(obtained, playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards()));
                        } else {
                            CLI.getInstance().unexpected();
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
        if(line.equals("x")) {
            client.setState(new StartTurn());
        } else {
            String[] words = Strings.splitLine(line);

            if(words.length != 2) {
                CLI.getInstance().marbleMarketSelection();
            } else {
                String sel = words[0];
                if(!sel.equals("row") && !sel.equals("col")) {
                    CLI.getInstance().marbleMarketSelection();
                } else {
                    try {
                        index = Integer.parseInt(words[1]);
                        MarbleMarket market = client.getModel().getGameZone().getMarbleMarket();
                        if(sel.equals("row")) {
                            rowColSel = true;
                            if(index < 0 || index >= market.getRows()) {
                                CLI.getInstance().marbleMarketSelection();
                                return;
                            }
                        } else {
                            rowColSel = false;
                            if(index < 0 || index >= market.getColumns()) {
                                CLI.getInstance().marbleMarketSelection();
                                return;
                            }
                        }
                        JsonObject message = new JsonObject();
                        message.addProperty("rowColSel", rowColSel);
                        message.addProperty("index", index);

                        JsonObject json = new JsonObject();
                        json.addProperty("command", "market");
                        json.add("message", message);

                        client.write(json.toString());
                    } catch (NumberFormatException e) {
                        CLI.getInstance().marbleMarketSelection();
                    }
                }
            }
        }
    }
}
