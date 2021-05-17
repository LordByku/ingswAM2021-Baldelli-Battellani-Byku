package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerDiscardLeaderCard;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerPlayLeaderCard;
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

    protected void handleSelection(Client client){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", "endTurn");

        client.write(jsonObject.toString());
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int selection = Integer.parseInt(line);

            switch (selection) {
                case 1: {
                    handleSelection(client);
                    break;
                }
                case 2: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                        client.setState(new SinglePlayerPlayLeaderCard(EndTurn::new));
                    }
                    else{
                        client.setState(new PlayLeaderCard(EndTurn::new));
                    }
                    break;
                }
                case 3: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                        client.setState(new SinglePlayerDiscardLeaderCard(EndTurn::new));
                    }
                    else{
                        client.setState(new DiscardLeaderCard(EndTurn::new));
                    }
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
