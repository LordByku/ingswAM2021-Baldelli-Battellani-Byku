package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.localModel.CardMarket;
import it.polimi.ingsw.network.client.localModel.FaithTrack;
import it.polimi.ingsw.network.client.localModel.MarbleMarket;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import java.util.ArrayList;
import java.util.HashSet;

public class CLI {
    private static CLI instance;

    private CLI() {}

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
        System.out.println("[1] MultiPlayer Game");
        System.out.println("[2] SinglePlayer Game");
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
        System.out.println("Error: an unknown message has been received");
    }

    public void connectionError() {
        System.out.println("There was an error connecting to the server");
    }

    public void connecting() {
        System.out.println("Connecting to server...");
    }

    public void serverError(String message) {
        System.out.println("Server error: " + message);
    }

    public void loadMultiPlayer() {
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

    public void spendResourcesStrongbox(){

        System.out.println("Select resources you want to spend from strongbox: [num of resources to select] [type of resource]. Or type \"warehouse\" to select resources from warehouse.");
    }

    public void spendResourcesWarehouse(){
        System.out.println("Select resources you want to spend from each depot of warehouse: [depotIndex] [num of resources to select]. Or type \"strongbox\" to select resources from strongbox.");
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
        System.out.println("[1] Collect resources from market");
        System.out.println("[2] Purchase development card");
        System.out.println("[3] Activate production effects");
        System.out.println("[4] Play leader card");
        System.out.println("[5] Discard leader card");
        System.out.println("[0] Check board state");
        System.out.println("Insert your choice:");
    }

    public void viewState() {
        System.out.println("[1] Check Marble Market");
        System.out.println("[2] Check Card Market");
        System.out.println("[3] Check player's board");
        System.out.println("[x] Back");
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

    public void manageWarehouse(ConcreteResourceSet obtained) {
        System.out.println("You have to assign: " + obtained.getCLIString());
        System.out.println("[1] Add to depot");
        System.out.println("[2] Remove from depot");
        System.out.println("[3] Swap from depots");
        System.out.println("[0] Confirm warehouse");
        System.out.println("Insert your choice:");
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
        System.out.println("[1] End Turn");
        System.out.println("[2] Play Leader Card");
        System.out.println("[3] Discard Leader Card");
        System.out.println("[0] Check board state");
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

    public void productionSelection(ArrayList<ArrayList<Integer>> devCardDecks, ArrayList<Integer> playedLeaderCards) {

        System.out.println("[0] " + LocalConfig.getInstance().getDefaultProductionPower().getCLIString());
        if(!devCardDecks.get(0).isEmpty()) {
            System.out.println("[1] " + DevCardsParser.getInstance().getCard(devCardDecks.get(0).get(devCardDecks.get(0).size()-1)).getProductionPower().getCLIString());
        }
        if(!devCardDecks.get(1).isEmpty())
            System.out.println("[2] " + DevCardsParser.getInstance().getCard(devCardDecks.get(1).get(devCardDecks.get(1).size()-1)).getProductionPower().getCLIString());
        if(!devCardDecks.get(2).isEmpty())
            System.out.println("[3] " + DevCardsParser.getInstance().getCard(devCardDecks.get(2).get(devCardDecks.get(2).size()-1)).getProductionPower().getCLIString());


        for (int i =0, index = 4; i<playedLeaderCards.size();i++){
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCards.get(i));
            if(leaderCard.isType(LeaderCardType.PRODUCTION))
                System.out.println("["+(index++)+"] " + leaderCard.getEffectString());

        }


        System.out.println("[x] Back");
    }

    public void activateProductionSelection () {
        System.out.println("Select the productions: ");
    }
}
