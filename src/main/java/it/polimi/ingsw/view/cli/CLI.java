package it.polimi.ingsw.view.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.game.actionTokens.ActionToken;
import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.windows.CLIWindow;
import it.polimi.ingsw.view.cli.windows.EndGameWindow;
import it.polimi.ingsw.view.cli.windows.InitWindow;
import it.polimi.ingsw.view.cli.windows.LobbyWindow;
import it.polimi.ingsw.view.cli.windows.viewWindows.CLIViewWindow;
import it.polimi.ingsw.view.cli.windows.viewWindows.ViewModel;
import it.polimi.ingsw.view.localModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CLI implements ViewInterface {
    private static CLIWindow cliWindow;
    private static CLIViewWindow cliViewWindow;
    private boolean pendingUpdate;

    public CLI() {
        pendingUpdate = false;
        cliWindow = null;
        cliViewWindow = null;
    }

    public static void selectNickname() {
        System.out.println("Insert your nickname to continue:");
    }

    public CLIViewWindow getViewWindow() {
        return cliViewWindow;
    }

    public static void setViewWindow(CLIViewWindow cliViewWindow) {
        CLI.cliViewWindow = cliViewWindow;
    }

    public static CLIWindow getActiveWindow() {
        if (cliViewWindow != null) {
            return cliViewWindow;
        }
        return cliWindow;
    }

    public void refreshWindow(Client client) {
        cliWindow = CLIWindow.refresh(client);
    }

    public static void renderGameWindow(Client client) {
        if (getActiveWindow() != null) {
            System.out.println();
            getActiveWindow().render(client);
            if (cliViewWindow == null) {
                System.out.println("[v] View Board State");
            } else {
                System.out.println("[v] Close View");
            }
        }
    }

    public static void welcome() {
        System.out.println("---- Masters of Renaissance ----");
    }

    public static void selectMode() {
        System.out.println();
        System.out.println("[0] Play Online");
        System.out.println("[1] Play Offline");
        System.out.println("Insert your choice:");
    }

    public static void playerList(ArrayList<String> nicknames, String hostname) {
        System.out.println();
        System.out.println("Current lobby:");

        for (String nickname : nicknames) {
            if (nickname.equals(hostname)) {
                System.out.println(nickname + TextColour.BLUE.escape() + " [HOST]" + TextColour.RESET);
            } else {
                System.out.println(nickname);
            }
        }
    }

    public static void host() {
        System.out.println("Press ENTER to start the game:");
    }

    public static void waitStart() {
        System.out.println("Waiting for host to start the game...");
    }

    public static void unexpected() {
        System.out.println(TextColour.RED.escape() + "Error: an unknown message has been received" + TextColour.RESET);
    }

    public static void connectionError() {
        System.out.println("There was an error connecting to the server");
    }

    public static void connecting() {
        System.out.println("Connecting to server...");
    }

    public static void error(String message) {
        System.out.println(TextColour.RED.escape() + "Error: " + message + TextColour.RESET);
    }

    public static void loadGame() {
        System.out.println("Game is loading...");
    }

    public static void showLeaderCards(ArrayList<Integer> leaderCardIDs) {
        for (int i = 0; i < leaderCardIDs.size(); i++) {
            System.out.println("[" + i + "]");
            if (leaderCardIDs.get(i) == null) {
                System.out.println("[ -- HIDDEN -- ]");
            } else {
                System.out.println(LeaderCardsParser.getInstance().getCard(leaderCardIDs.get(i)).getCLIString());
            }
        }
    }

    public static void initDiscard() {
        System.out.println("Insert the indices of the two Leader Cards you want to discard:");
    }

    public static void discardLeaderCard() {
        System.out.println("Insert the index of the leader card you want to discard or press [x] to go back:");
    }

    public static void playLeaderCard() {
        System.out.println("Insert the index of the leader card you want to play or press [x] to go back:");
    }

    public static void purchaseSelectRowAndCol() {
        System.out.println("Insert row and column of the card you want to buy from the card market:");
        System.out.println("[x] Back");
    }

    public static void purchaseSelectDeckIndex() {
        System.out.println("Insert the index of the deck you want to place the card in:");
        System.out.println("[x] Back");
    }

    public static void waitInitDiscard() {
        System.out.println("Waiting for players to discard Leader Cards...");
    }

    public static void initResources(int selectionsLeft, int currentDepotIndex) {
        switch (selectionsLeft) {
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

    public static void waitInitResources() {
        System.out.println("Waiting for players to select resources...");
    }

    public static void waitTurn() {
        System.out.println("Waiting for other players to complete their turn...");
    }

    public static void actionToken(LocalModel model) {
        if(model != null){
            ActionToken actionToken = model.getGameZone().getActionToken();
            if (actionToken != null) {
                System.out.println(actionToken.getCLIString());
                model.getGameZone().resetActionToken();
            }
        }
    }

    public static void startTurn() {
        System.out.println("[0] Collect resources from market");
        System.out.println("[1] Purchase development card");
        System.out.println("[2] Activate production effects");
        System.out.println("[3] Play leader card");
        System.out.println("[4] Discard leader card");
        System.out.println("Insert your choice:");
    }

    public static void marbleMarket(MarbleMarket marbleMarket) {
        System.out.println(marbleMarket.getCLIString());
    }

    public static void cardMarket(CardMarket cardMarket) {
        System.out.println(cardMarket.getCLIString());
    }

    public static void showDevCard(Integer devCardID) {
        if (devCardID == null) {
            System.out.println("This deck is empty");
        } else {
            DevCard devCard = DevCardsParser.getInstance().getCard(devCardID);
            System.out.println(devCard.getCLIString());
        }
    }

    public static void boardComponentSelection(Board board) {
        System.out.println(board.getCLIString());
    }

    public static void showFaithTrack(FaithTrack faithTrack) {
        System.out.println(faithTrack.getCLIString());
    }

    public static void showWarehouse(ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        ArrayList<Integer> depotSizes = LocalConfig.getInstance().getDepotSizes();
        int maxDepotSize = 0;
        for (int depotSize : depotSizes) {
            maxDepotSize = Math.max(2, depotSize);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, leaderCardIndex = 0; i < warehouse.size(); ++i) {
            result.append("[").append(i).append("] ");

            ConcreteResource resourceType = null;
            if (i == depotSizes.size()) {
                depotSizes.add(2);
                while (leaderCardIndex < playedLeaderCards.size()) {
                    LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCards.get(leaderCardIndex++));
                    if (leaderCard.isType(LeaderCardType.DEPOT)) {
                        resourceType = ((DepotLeaderCard) leaderCard).getType();
                        break;
                    }
                }
            }
            int delta = maxDepotSize - depotSizes.get(i);
            for (int j = 0; j < delta; ++j) {
                result.append(" ");
            }
            ConcreteResourceSet depot = warehouse.get(i);

            for (int j = 0; j < depot.size(); ++j) {
                result.append(depot.getResourceType().getCLIString()).append(" ");
            }
            for (int j = depot.size(); j < depotSizes.get(i); ++j) {
                if (resourceType == null) {
                    result.append(TextColour.WHITE.escape()).append("\u25ef").append(TextColour.RESET).append(" ");
                } else {
                    result.append(resourceType.getColour().escape()).append("\u25ef").append(TextColour.RESET).append(" ");
                }
            }

            if (i < warehouse.size() - 1) {
                result.append("\n");
            }
        }
        System.out.println(result);
    }

    public static void showStrongbox(ConcreteResourceSet strongBox) {
        System.out.println("[ " + strongBox.getCLIString() + "]");
    }

    public static void marbleMarketSelection() {
        System.out.println("Insert row or col followed by the index of your choice or press [x] to go back:");
    }

    public static void whiteMarbleSelection(ChoiceSet choiceSet, int choices) {
        if (choices == 1) {
            System.out.println("Select " + choices + " resource to obtain from white marbles (you can choose from " + choiceSet.getCLIString() + "):");
        } else {
            System.out.println("Select " + choices + " resources to obtain from white marbles (you can choose from " + choiceSet.getCLIString() + "):");
        }
    }

    public static void choiceResourceSelection(int choices) {
        if (choices == 1) {
            System.out.println("Select " + choices + " resource to obtain:");
        } else {
            System.out.println("Select " + choices + " resources to obtain:");
        }
    }

    public static void manageWarehouse(ConcreteResourceSet obtained) {
        System.out.println("You have to assign: " + obtained.getCLIString());
        System.out.println("To add resources to a depot     : add [depotIndex] [resource] [resource]...");
        System.out.println("To remove resources from a depot: remove [depotIndex] [resource] [resource]...");
        System.out.println("To swap resources of two depots : swap [depotIndexA] [depotIndexB]");
        System.out.println("To confirm the warehouse        : confirm");
    }

    public static void endTurn() {
        System.out.println("[0] End Turn");
        System.out.println("[1] Play Leader Card");
        System.out.println("[2] Discard Leader Card");
        System.out.println("Insert your choice:");
    }

    public static void selectDevCardDeck() {
        System.out.println("[0] Check first deck");
        System.out.println("[1] Check second deck");
        System.out.println("[2] Check third deck");
        System.out.println("[x] Back");
    }

    public static void showDevCardDeck(ArrayList<Integer> devCardIDs) {
        //Maybe to be adjusted

        DevCardDeck deck = new DevCardDeck();

        for (int i : devCardIDs)
            deck.add(DevCardsParser.getInstance().getCard(i));
        System.out.println(deck.getCLIString());
    }

    public static void activateProductionSelection(HashMap<Integer, ProductionDetails> map) {
        System.out.println("Select all the productions to activate: ");
        for (Map.Entry<Integer, ProductionDetails> entry : map.entrySet()) {
            System.out.println("[" + entry.getKey() + "] " + entry.getValue().getCLIString());
        }
        System.out.println("[x] Back");
    }

    public static void reconnecting(int timerDelay) {
        System.out.println("Trying to reconnect in " + timerDelay / 1000 + " seconds...");
    }

    public static void spendResources(ChoiceResourceSet toSpend, ConcreteResourceSet currentSelection) {
        System.out.println("Resources required: " + toSpend.getCLIString());
        System.out.println("Resources selected: " + currentSelection.getCLIString());
        System.out.println("To select resources from warehouse: warehouse [depotIndex] [resource] [resource]...");
        System.out.println("To select resources from strongbox: strongbox [resource] [resource]...");
        System.out.println("[x] Back");
    }

    public static void showModel(LocalModel model) {
        System.out.println(model.getCLIString());
    }

    public static void endGame(JsonObject endGameMessage) {
        JsonArray results = endGameMessage.getAsJsonArray("results");
        int maxPoints = -1, maxResources = -1;
        ArrayList<String> winner = new ArrayList<>();

        for(JsonElement jsonElement: results) {
            JsonObject playerObject = jsonElement.getAsJsonObject();
            String player = playerObject.get("player").getAsString();
            int basePoints = playerObject.get("basePoints").getAsInt();
            int resources = playerObject.get("resources").getAsInt();

            int totalPoints = basePoints + resources / 5;
            if(totalPoints >= maxPoints) {
                if(totalPoints > maxPoints || resources > maxResources) {
                    winner.clear();
                    winner.add(player);
                } else if(resources == maxResources) {
                    winner.add(player);
                }
            }
        }

        results();
        for(JsonElement jsonElement: results) {
            JsonObject playerObject = jsonElement.getAsJsonObject();
            String player = playerObject.get("player").getAsString();
            int basePoints = playerObject.get("basePoints").getAsInt();
            int resources = playerObject.get("resources").getAsInt();

            int totalPoints = basePoints + resources / 5;

            playerResults(player, totalPoints);
        }

        if(endGameMessage.has("computerWin")) {
            singlePlayerWinner(endGameMessage.get("computerWin").getAsBoolean());
        } else {
            multiPlayerWinner(winner);
        }
    }

    public static void results() {
        System.out.println("Game has finished");
        System.out.println("Results:");
    }

    public static void playerResults(String nickname, int points) {
        System.out.println(nickname + " : " + points + " points");
    }

    public static void singlePlayerWinner(boolean computerWin) {
        System.out.println();
        if(computerWin) {
            System.out.println(TextColour.GREEN.escape() + "Lorenzo il Magnifico has won" + TextColour.RESET);
        } else {
            System.out.println(TextColour.GREEN.escape() + "You have won" + TextColour.RESET);
        }
        System.out.println("[x] Exit");
    }

    public static void multiPlayerWinner(ArrayList<String> winner) {
        System.out.println();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < winner.size(); ++i) {
            if(i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(winner.get(i));
        }
        if(winner.size() == 1) {
            System.out.println(TextColour.GREEN.escape() + "The winner is " + winner.get(0) + TextColour.RESET);
        } else {
            System.out.println(TextColour.GREEN.escape() + "The winners are " + stringBuilder.toString() + " (Tie)" + TextColour.RESET);
        }
    }

    public static void closing() {
        System.out.println("Lost connection with server, closing client...");
    }

    @Override
    public void onError(Client client, String message) {
        error(message);
        refreshWindow(client);
        renderGameWindow(client);
    }

    @Override
    public void onCommand(Client client, String player, CommandBuffer commandBuffer) {
        if (pendingUpdate || player.equals(client.getNickname())) {
            pendingUpdate = false;
            refreshWindow(client);
            renderGameWindow(client);
        }
    }

    @Override
    public void onUpdate(Client client) {
        CLIWindow currentWindow = getActiveWindow();
        if (currentWindow == null || currentWindow.refreshOnUpdate(client)) {
            pendingUpdate = true;
        }
    }

    @Override
    public void onUserInput(Client client, String line) {
        if (line.equals("v")) {
            if (getViewWindow() == null) {
                setViewWindow(new ViewModel());
            } else {
                setViewWindow(null);
            }
            renderGameWindow(client);
            return;
        }
        getActiveWindow().handleUserMessage(client, line);
    }

    @Override
    public void onUnexpected(Client client) {
        unexpected();
    }

    @Override
    public void onEndGame(Client client, JsonObject endGameMessage) {
        client.getModel().setEndGame();
        cliWindow = new EndGameWindow(endGameMessage);
        renderGameWindow(client);
    }

    @Override
    public void init(Client client) {
        CLI.welcome();
        cliWindow = new InitWindow();
        cliWindow.render(client);
    }

    @Override
    public void welcome(Client client) {
        cliWindow.render(client);
    }

    @Override
    public void loadGame(Client client) {
        CLI.loadGame();
    }

    @Override
    public void updatePlayerList(Client client, ArrayList<String> nicknames, String hostNickname) {
        CLI.playerList(nicknames, hostNickname);
        cliWindow = new LobbyWindow();
        cliWindow.render(client);
    }

    @Override
    public void startGame(Client client, String line) {
        cliWindow.handleUserMessage(client, line);
    }
}
