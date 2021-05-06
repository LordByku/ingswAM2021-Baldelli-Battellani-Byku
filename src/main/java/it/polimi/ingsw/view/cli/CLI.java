package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.parsing.LeaderCardsParser;

import java.util.ArrayList;

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
        System.out.println("Press ENTER to start the game");
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
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();

        for (Integer leaderCardID : leaderCardIDs) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardID);
            leaderCard.addCLISupport();
            leaderCards.add(leaderCard);
        }

        for(int i = 0; i < leaderCards.size(); i++) {
            System.out.println("[" + i + "]");
            System.out.println(leaderCards.get(i));
            System.out.println();
        }
    }
}
