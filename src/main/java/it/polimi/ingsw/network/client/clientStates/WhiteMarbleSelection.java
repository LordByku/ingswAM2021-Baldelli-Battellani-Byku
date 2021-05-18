package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerWhiteMarbleSelection;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;

public class WhiteMarbleSelection extends ClientState {
    private final ChoiceSet choiceSet;
    private final int choices;

    public WhiteMarbleSelection(ChoiceSet choiceSet, int choices) {
        this.choiceSet = choiceSet;
        this.choices = choices;
        CLI.getInstance().whiteMarbleSelection(choiceSet, choices);
    }

    public static ClientState builder(ChoiceSet choiceSet, int choices) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerWhiteMarbleSelection(choiceSet, choices);
        } else {
            return new WhiteMarbleSelection(choiceSet, choices);
        }
    }

    protected void selectionToCLI() {
        CLI.getInstance().whiteMarbleSelection(choiceSet, choices);
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                selectionToCLI();
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "confirm": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        ConcreteResourceSet concreteResourceSet = ClientParser.getInstance().getConcreteResourceSet(message.getAsJsonObject("obtained"));

                        Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
                        CLI.getInstance().showWarehouse(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards());
                        client.setState(ManageWarehouse.builder(concreteResourceSet, playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards()));

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

    protected void handleSelection(Client client, ArrayList<ConcreteResource> resources) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", "whiteMarble");
        jsonObject.add("resources", ClientParser.getInstance().serialize(resources));

        client.write(jsonObject.toString());
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);

        if(words.length != choices) {
            selectionToCLI();
        } else {
            ArrayList<ConcreteResource> resources = new ArrayList<>();

            for(String word: words) {
                ConcreteResource resource = ClientParser.getInstance().readUserResource(word);
                if(resource == null) {
                    selectionToCLI();
                    return;
                }
                resources.add(resource);
            }

            handleSelection(client, resources);
        }
    }
}
