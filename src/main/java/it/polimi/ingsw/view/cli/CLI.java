package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.localModel.*;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.cli.windows.CLIWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CLI {
    private static CLI instance;

    private CLI() {

    }

    public static CLI getInstance() {
        if(instance == null) {
            instance = new CLI();
        }
        return instance;
    }

    public void welcome() {
        System.out.println("---- Masters of Renaissance ----");
    }

    public void selectNickname() {
        System.out.println("Insert your nickname to continue:");
    }

    public void selectMode() {
        System.out.println("[0] MultiPlayer Game");
        System.out.println("[1] SinglePlayer Game");
        System.out.println("Insert your choice:");
    }

    public void playerList(ArrayList<String> nicknames, String hostname) {
        System.out.println("Current lobby:");

        for(String nickname: nicknames) {
            if(nickname.equals(hostname)) {
                System.out.println(nickname + TextColour.BLUE.escape() + " [HOST]" + TextColour.RESET);
            } else {
                System.out.println(nickname);
            }
        }
    }

    public void host() {
        System.out.println("Press ENTER to start the game:");
    }

    public void waitStart() {
        System.out.println("Waiting for host to start the game...");
    }

    public void unexpected() {
        System.out.println(TextColour.RED.escape() + "Error: an unknown message has been received" + TextColour.RESET);
    }

    public void connectionError() {
        System.out.println("There was an error connecting to the server");
    }

    public void connecting() {
        System.out.println("Connecting to server...");
    }

    public void serverError(String message) {
        System.out.println(TextColour.RED.escape() + "Server error: " + message + TextColour.RESET);
    }

    public void loadGame() {
        System.out.println("Game is loading...");
    }

    public void showLeaderCards(ArrayList<Integer> leaderCardIDs) {
        for(int i = 0; i < leaderCardIDs.size(); i++) {
            System.out.println("[" + i + "]");
            if(leaderCardIDs.get(i) == null) {
                System.out.println("[ -- HIDDEN -- ]");
            } else {
                System.out.println(LeaderCardsParser.getInstance().getCard(leaderCardIDs.get(i)).getCLIString());
            }
        }
    }

    public void initDiscard() {
        System.out.println("Insert the indices of the two Leader Cards you want to discard:");
    }

    public void discardLeaderCard(){
        System.out.println("Insert the index of the leader card you want to discard or press [x] to go back:");
    }

    public void playLeaderCard(){
        System.out.println("Insert the index of the leader card you want to play or press [x] to go back:");
    }

    public void purchaseDevCard(){
        System.out.println("Insert the rowIndex and the columnIndex of the card you want to buy and the index of the deck you want to place the card in; or press [x] to go back:");
        System.out.println("[x]back\nrowIndex - columnIndex - deckIndex");
    }

    public void showWarehouseAndStrongbox(ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards, ConcreteResourceSet strongBox){
        System.out.println("WAREHOUSE");
        showWarehouse(warehouse,playedLeaderCards);
        System.out.println("STRONGBOX");
        showStrongbox(strongBox);
    }

    public void toSpend(ConcreteResourceSet toSpend){
        System.out.println("Resources to spend: " + toSpend.getCLIString());
    }

    public void spendResourcesWarehouse(){
        System.out.println("Select resources you want to spend from warehouse: [depotIndex] [num of resources to select]. Or type \"strongbox\" to select resources from strongbox.");
    }

    public void spendResourcesStrongbox(){

        System.out.println("Select resources you want to spend from strongbox: [num of resources to select] [type of resource]. Or type \"warehouse\" to select resources from warehouse.");
    }

    public void waitInitDiscard() {
        System.out.println("Waiting for other players to discard their Leader Cards...");
    }

    public void initResources(int selectionsLeft, int currentDepotIndex) {
        switch(selectionsLeft) {
            case 0:
                waitInitResources();
                break;
            case 1:
                System.out.println("Insert the resources you want to put in the depot [" + currentDepotIndex + "] or press ENTER to continue to the next depot (you have one resource left to assign):");
                break;
            default:
                System.out.println("Insert the resources you want to put in the depot [" + currentDepotIndex + "] or press ENTER to continue to the next depot (you have " + selectionsLeft + " resources left to assign):");
                break;
        }
    }

    public void waitInitResources() {
        System.out.println("Waiting for other players to select their resources...");
    }

    public void waitTurn() {
        System.out.println("Waiting for other players to complete their turn...");
    }

    public void otherPlayersTurn() {
        System.out.println("Not your turn");
    }

    public void startTurn() {
        System.out.println("[0] Collect resources from market");
        System.out.println("[1] Purchase development card");
        System.out.println("[2] Activate production effects");
        System.out.println("[3] Play leader card");
        System.out.println("[4] Discard leader card");
        System.out.println("[v] Check board state");
        System.out.println("Insert your choice:");
    }

    public void marbleMarket(MarbleMarket marbleMarket) {
        System.out.println(marbleMarket.getCLIString());
    }

    public void cardMarket(CardMarket cardMarket) {
        System.out.println(cardMarket.getCLIString());
        System.out.println("Insert row and column of the deck you want to check or press [x] to go back:");
    }

    public void showDevCard(Integer devCardID) {
        if(devCardID == null) {
            System.out.println("This deck is empty");
        } else {
            DevCard devCard = DevCardsParser.getInstance().getCard(devCardID);
            System.out.println(devCard.getCLIString());
        }
    }

    public void selectPlayer(ArrayList<String> nicknames) {
        for(int i = 0; i < nicknames.size(); ++i) {
            System.out.println("[" + i + "] " + nicknames.get(i));
        }
        System.out.println("Select the player board to check or press [x] to go back:");
    }

    public void boardComponentSelection() {
        System.out.println("[1] Check Faith Track");
        System.out.println("[2] Check Warehouse");
        System.out.println("[3] Check Strongbox");
        System.out.println("[4] Check Development Cards");
        System.out.println("[5] Check Played Leader Cards");
        System.out.println("[6] Check Hand Leader Cards");
        System.out.println("[x] Back");
    }

    public void showFaithTrack(FaithTrack faithTrack) {
        System.out.println(faithTrack.getCLIString());
    }

    public void showWarehouse(ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        ArrayList<Integer> depotSizes = LocalConfig.getInstance().getDepotSizes();
        int maxDepotSize = 2;
        for(int depotSize: depotSizes) {
            maxDepotSize = Math.max(2, depotSize);
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0, leaderCardIndex = 0; i < warehouse.size(); ++i) {
            result.append("[").append(i).append("] ");

            ConcreteResource resourceType = null;
            if(i == depotSizes.size()) {
                depotSizes.add(2);
                while(leaderCardIndex < playedLeaderCards.size()) {
                    LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCards.get(leaderCardIndex++));
                    if(leaderCard.isType(LeaderCardType.DEPOT)) {
                        resourceType = ((DepotLeaderCard) leaderCard).getType();
                        break;
                    }
                }
            }
            int delta = maxDepotSize - depotSizes.get(i);
            for(int j = 0; j < delta; ++j) {
                result.append(" ");
            }
            ConcreteResourceSet depot = warehouse.get(i);

            for(int j = 0; j < depot.size(); ++j) {
                result.append(depot.getResourceType().getCLIString()).append(" ");
            }
            for(int j = depot.size(); j < depotSizes.get(i); ++j) {
                if(resourceType == null) {
                    result.append(TextColour.WHITE.escape()).append("\u25ef").append(TextColour.RESET).append(" ");
                } else {
                    result.append(resourceType.getColour().escape()).append("\u25ef").append(TextColour.RESET).append(" ");
                }
            }

            if(i < warehouse.size() - 1) {
                result.append("\n");
            }
        }
        System.out.println(result);
    }

    public void showStrongbox(ConcreteResourceSet strongBox) {
        System.out.println(strongBox.getCLIString());
    }

    public void discardLeaderCardSuccess() {
        System.out.println("Leader Card successfully discarded");
    }

    public void playLeaderCardSuccess() {
        System.out.println("Leader Card successfully activated");
    }

    public void purchaseDevCardSuccess(){
        System.out.println("Development Card successfully purchased");
    }
    public void spendResourcesSuccess(){
        System.out.println("Resources spent successfully");
    }

    public void marbleMarketSelection() {
        System.out.println("Insert row or col followed by the index of your choice or press [x] to go back:");
    }

    public void whiteMarbleSelection(ChoiceSet choiceSet, int choices) {
        if(choices == 1) {
            System.out.println("Select " + choices + " resource to obtain from white marbles (you can choose from " + choiceSet.getCLIString() + "):");
        } else {
            System.out.println("Select " + choices + " resources to obtain from white marbles (you can choose from " + choiceSet.getCLIString() + "):");
        }
    }

    public void choiceResourceSelection(int choices) {
        if(choices == 1) {
            System.out.println("Select " + choices + " resource to obtain:");
        } else {
            System.out.println("Select " + choices + " resources to obtain:");
        }
    }

    public void manageWarehouse(ConcreteResourceSet obtained) {
        System.out.println("You have to assign: " + obtained.getCLIString());
        System.out.println("To add resources to a depot     : add [depotIndex] [resource] [resource]...");
        System.out.println("To remove resources from a depot: remove [depotIndex] [resource] [resource]...");
        System.out.println("To swap resources of two depots : swap [depotIndexA] [depotIndexB]");
        System.out.println("To confirm the warehouse        : confirm");
    }

    public void addToDepot() {
        System.out.println("Insert the index of the depot followed by the resources you want to add:");
    }

    public void removeFromDepot() {
        System.out.println("Insert the index of the depot followed by the resources you want to remove:");
    }

    public void swapFromDepots() {
        System.out.println("Insert the indices of the two depots you want to swap:");
    }

    public void endTurn() {
        System.out.println("[0] End Turn");
        System.out.println("[1] Play Leader Card");
        System.out.println("[2] Discard Leader Card");
        System.out.println("[v] Check board state");
        System.out.println("Insert your choice:");
    }

    public void selectDevCardDeck() {
        System.out.println("Select the area to check or press [x] to go back:" );
    }

    public void showDevCardDeck(ArrayList<Integer> devCardIDs){
        //Maybe to be adjusted

        DevCardDeck deck = new DevCardDeck();

        for(int i : devCardIDs)
            deck.add(DevCardsParser.getInstance().getCard(i));
        System.out.println(deck.getCLIString());
    }

    public void productionSelection(HashMap<Integer, ProductionDetails> map) {
        for(Map.Entry<Integer, ProductionDetails> entry: map.entrySet()) {
            int index = entry.getKey();
            ProductionDetails productionDetails = entry.getValue();
            System.out.println("[" + index + "] " + productionDetails.getCLIString());
        }
        System.out.println("[x] Back");
    }

    public void activateProductionSelection () {
        System.out.println("Select all the productions to activate: ");
    }

    public void purchaseMenu() {
        System.out.println("[0] Show Card Market");
        System.out.println("[1] Select Card to Purchase");
        System.out.println("[x] Back");
    }

    public void reconnecting(int timerDelay) {
        System.out.println("Trying to reconnect in " + timerDelay / 1000 + " seconds...");
    }

    public void spendResources(ChoiceResourceSet toSpend, ConcreteResourceSet currentSelection) {
        System.out.println("Resources required: " + toSpend.getCLIString());
        System.out.println("Resources selected: " + currentSelection.getCLIString());
        System.out.println("To select resources from warehouse: warehouse [depotIndex] [resource] [resource]...");
        System.out.println("To select resources from strongbox: strongbox [resource] [resource]...");
    }
}
