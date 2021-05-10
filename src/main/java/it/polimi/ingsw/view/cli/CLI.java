package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.localModel.CardMarket;
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

    public void showLeaderCards(ArrayList<Integer> leaderCardIDs, HashSet<Integer> selections) {
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();

        for (Integer leaderCardID : leaderCardIDs) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardID);
            leaderCard.addCLISupport();
            leaderCards.add(leaderCard);
        }

        for(int i = 0; i < leaderCards.size(); i++) {
            if(selections.contains(i)) {
                System.out.println(TextColour.RED.escape() + "[" + i + "]" + TextColour.RESET);
            } else {
                System.out.println("[" + i + "]");
            }
            System.out.println(leaderCards.get(i).getCLIString());
            System.out.println();
        }
    }

    public void initDiscard() {
        System.out.println("Insert the indices of the two Leader Cards you want to discard:");
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
}
