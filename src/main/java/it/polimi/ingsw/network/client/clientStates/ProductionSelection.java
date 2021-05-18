package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.*;

public class ProductionSelection extends ClientState {

    public ProductionSelection() {
        CLI.getInstance().activateProductionSelection();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                client.setState(new ProductionSelection());
                break;
            }

            case "ok": {
                String type = ClientParser.getInstance().getType(json);
                switch (type) {
                    case "confirm": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
 //                       client.setState(new SpendResourcesProduction());
                        //spend resources
                        //successivamente inserire le risorse da ottenere
                        // poi alla fine fare update
                        break;
                    }
                    default:{
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
        if(words[words.length - 1].equals("x")){
            client.setState(new StartTurn());
        } else {
            try {
                ArrayList<Integer> prodChoices = new ArrayList<>();
               for(int i=0; i < words.length; i++) {
                   prodChoices.add(Integer.parseInt(words[i]));
                   if (prodChoices.get(i) < 0 || prodChoices.get(i) > Collections.max(prodChoices))
                       client.setState(new ProductionSelection());
               }
               //check duplicates

                Set<Integer> prodChoicesSet = new HashSet<Integer>(prodChoices);
                   if(prodChoices.size()> prodChoicesSet.size())
                       client.setState(new ProductionSelection());
                   else{

                       JsonObject jsonObject = new JsonObject();

                       jsonObject.addProperty("command", "production");

                       JsonArray jsonArray = new JsonArray();
                       for (Integer i: prodChoices)
                           jsonArray.add(i);

                       jsonObject.add("message", jsonArray);

                       client.write(jsonObject.toString());

                   }

            } catch (NumberFormatException e) {
                client.setState(new ProductionSelection());
                //TODO mettere cli al posto di newstate
            }
        }
    }

}
