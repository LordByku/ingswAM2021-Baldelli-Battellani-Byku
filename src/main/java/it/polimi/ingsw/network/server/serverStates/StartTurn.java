package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Consumer;

public class StartTurn extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        if(clientHandler.getPerson().isActivePlayer()) {
            JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();
            String command = ServerParser.getInstance().getCommand(json);

            switch(command) {
                case "purchase": {
                    JsonObject message = ServerParser.getInstance().getMessage(json).getAsJsonObject();

                    int row = message.get("row").getAsInt();
                    int column = message.get("column").getAsInt();
                    int deckIndex = message.get("deckIndex").getAsInt();

                    if(Controller.getInstance().purchase(clientHandler.getPerson(), row,column,deckIndex)){
                        clientHandler.confirm();
                        clientHandler.setState(new SpendResources(deckIndex));
                    }
                    else{
                        clientHandler.error("Can't buy this card, not enough resources or not invalid top deck level");
                    }

                    break;
                }
                case "production": {
                    JsonObject message = ServerParser.getInstance().getMessage(json).getAsJsonObject();

                    int[] activeSet = ServerParser.getInstance().parseIntArray(message.getAsJsonArray("activeSet"));

                    if(Controller.getInstance().checkProdIndex(clientHandler.getPerson(),activeSet)) {
                        if (Controller.getInstance().canProduce(clientHandler.getPerson(), activeSet)) {
                            CLI.getInstance().success();
                            clientHandler.confirm();
                            clientHandler.setState(new SpendResourcesProduction());
                            //clientHandler.setState(new ProductionSelection());
                        } else {
                            clientHandler.error("Not enough resources for all chosen productions");
                        }
                    }
                    else {
                        clientHandler.error("The productions selected contain at least one production not existing");
                    }

                    break;
                }
                case "market": {
                    JsonObject message = ServerParser.getInstance().getMessage(json).getAsJsonObject();

                    boolean rowColSel = message.get("rowColSel").getAsBoolean();
                    int index = message.get("index").getAsInt();

                    if(Controller.getInstance().market(clientHandler.getPerson(), rowColSel, index)) {
                        if(Controller.getInstance().toObtainIsConcrete()) {
                            JsonObject outMessage = new JsonObject();
                            outMessage.add("obtained", ServerParser.getInstance().serialize(Controller.getInstance().concreteToObtain()));
                            clientHandler.ok("confirm", outMessage);
                            clientHandler.setState(new ManageWarehouse());
                        } else {
                            JsonObject outMessage = new JsonObject();
                            outMessage.addProperty("choices", Controller.getInstance().toObtainChoices());
                            clientHandler.ok("confirm", outMessage);
                            clientHandler.setState(new WhiteMarbleSelection());
                        }
                    } else {
                        clientHandler.error("Invalid choice");
                    }

                    break;
                }
                case "leaderCard": {
                    JsonObject message = ServerParser.getInstance().getMessage(json).getAsJsonObject();
                    int leaderCardIndex = message.get("leaderCard").getAsInt();

                    switch(message.get("action").getAsString()) {
                        case "play": {
                            if(Controller.getInstance().playLeaderCard(clientHandler.getPerson(), leaderCardIndex)) {
                                Consumer<GameStateSerializer> lambda = (serializer) -> {
                                    serializer.addHandLeaderCards(clientHandler.getPerson());
                                    serializer.addPlayedLeaderCards(clientHandler.getPerson());
                                    serializer.addWarehouse(clientHandler.getPerson());
                                };

                                clientHandler.updateGameState(lambda);
                            } else {
                                clientHandler.error("Invalid choice");
                            }
                            break;
                        }
                        case "discard": {
                            if(Controller.getInstance().discardLeaderCard(clientHandler.getPerson(), leaderCardIndex)) {
                                Consumer<GameStateSerializer> lambda = (serializer) -> {
                                    serializer.addHandLeaderCards(clientHandler.getPerson());
                                    serializer.addFaithTrack(clientHandler.getPerson());
                                };

                                clientHandler.updateGameState(lambda);
                            } else {
                                clientHandler.error("Invalid choice");
                            }
                            break;
                        }
                        default: {
                            clientHandler.error("Invalid Leader Card Action");
                        }
                    }
                    break;
                }
                default: {
                    clientHandler.error("Invalid command");
                }
            }
        } else {
            clientHandler.error("Not your turn");
        }
    }
}
