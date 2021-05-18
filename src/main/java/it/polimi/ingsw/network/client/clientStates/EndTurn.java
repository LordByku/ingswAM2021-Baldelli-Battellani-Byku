package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.view.cli.CLI;

public class EndTurn extends ClientState {
    public EndTurn() {
        CLI.getInstance().endTurn();
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

                        if(!client.getModel().getPlayer(client.getNickname()).hasInkwell()) {
                            client.setState(new WaitTurn());
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
        try {
            int selection = Integer.parseInt(line);

            switch (selection) {
                case 1: {
                    if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                        Person person = Game.getInstance().getSinglePlayer();
                        Controller.getInstance().endTurn(person);
                        GameStateSerializer serializer = new GameStateSerializer(person.getNickname());
                        serializer.addFaithTrack(person);
                        serializer.addCardMarket();
                        client.getModel().updateModel(serializer.getMessage());
                        client.setState(new StartTurn());
                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("command", "endTurn");

                        client.write(jsonObject.toString());
                    }
                    break;
                }
                case 2: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    client.setState(PlayLeaderCard.builder(EndTurn::new));
                    break;
                }
                case 3: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    client.setState(DiscardLeaderCard.builder(EndTurn::new));
                    break;
                }
                case 0: {
                    client.setState(new ViewState(EndTurn::new));
                    break;
                }
            }
        } catch (NumberFormatException e) {
            CLI.getInstance().endTurn();
        }
    }
}
