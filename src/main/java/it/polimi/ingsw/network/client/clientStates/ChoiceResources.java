package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChoiceResources extends ClientState {

    private final int choices;

    public ChoiceResources(int choices) {
        this.choices = choices;
        //CLI.getInstance().whiteMarbleSelection(choiceSet, choices);
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
               // CLI.getInstance().whiteMarbleSelection(choiceSet, choices);
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "confirm": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();

                        ConcreteResourceSet concreteResourceSet = ClientParser.getInstance().getConcreteResourceSet(message.getAsJsonObject("obtained"));
                        CLI.getInstance().showWarehouse(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards());
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

    @Override
    public void handleUserMessage(Client client, String line) {

        String[] words = Strings.splitLine(line);

        if(words.length != choices) {
           // CLI.getInstance().whiteMarbleSelection(choiceSet, choices);

        } else {
            ArrayList<ConcreteResource> resources = new ArrayList<>();

            for(String word: words) {
                ConcreteResource resource = ClientParser.getInstance().readUserResource(word);
                if(resource == null) {
                  //  CLI.getInstance().whiteMarbleSelection( choices);
                    return;
                }
                resources.add(resource);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("command", "choiceResource");
            jsonObject.add("resources", ClientParser.getInstance().serialize(resources));

            client.write(jsonObject.toString());
        }
    }
}
