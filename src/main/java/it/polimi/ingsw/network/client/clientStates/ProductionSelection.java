package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.network.client.localModel.Player;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.*;

public class ProductionSelection extends ClientState {

    private ArrayList<Integer> prodChoices;

    public ProductionSelection() {
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
                        Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
                        CLI.getInstance().showWarehouseAndStrongbox(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards(), playerBoard.getStrongBox());
                        int numOfDepots = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().size();
                        SpendableResourceSet spendableResourceSet = new SpendableResourceSet();

                        if(prodChoices.contains(0)){
                            spendableResourceSet.union(BoardParser.getInstance().getDefaultProductionPower().getInput());

                        }
                        for(int i=1; i<=3;i++)
                            if (prodChoices.contains(i)) {
                                ArrayList <Integer> devCardDeck =playerBoard.getDevCardDeck(i - 1);
                                if(!devCardDeck.isEmpty()) {
                                    DevCard devCard = DevCardsParser.getInstance().getCard(devCardDeck.get(devCardDeck.size() - 1));
                                    spendableResourceSet.union(devCard.getProductionPower().getInput());

                                }
                                else {
                                    CLI.getInstance().activateProductionSelection();
                                    client.setState(new ProductionSelection());
                                }
                            }

                        LeaderCard leaderCard = null;

                        for( int i = 4; i<playerBoard.getPlayedLeaderCards().size()+4; i++) {
                            leaderCard = LeaderCardsParser.getInstance().getCard(playerBoard.getPlayedLeaderCards().get(i - 4));

                            if (prodChoices.contains(i) && leaderCard.isType(LeaderCardType.PRODUCTION)) {
                                ProductionLeaderCard productionLeaderCard = (ProductionLeaderCard) leaderCard;
                                spendableResourceSet.union(productionLeaderCard.getProductionPower().getInput());
                            }
                        }

                        //JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.setState(new SpendResourcesProductionWarehouse(numOfDepots,spendableResourceSet));
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

        if(words.length==1 && words[0].equals("x")){
            client.setState(new StartTurn());
        } else {
            try {
                Board playerBoard = ((Player) client.getModel().getPlayer(client.getNickname())).getBoard();
                 this.prodChoices = new ArrayList<>();
                 for(int i=0; i < words.length; i++) {
                     prodChoices.add(Integer.parseInt(words[i]));
                     if (prodChoices.get(i) < 0) {
                         CLI.getInstance().activateProductionSelection();
                         client.setState(new ProductionSelection());
                         return;
                     }

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

                       JsonObject message = new JsonObject();

                       message.add("activeSet", jsonArray);
                       jsonObject.add("message", message);
                       client.write(jsonObject.toString());

                   }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                CLI.getInstance().activateProductionSelection();
                client.setState(new ProductionSelection());
            }
        }
    }

}
