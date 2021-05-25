package it.polimi.ingsw.network.server;

import com.google.gson.*;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.PlayerType;
import it.polimi.ingsw.model.game.actionTokens.ActionTokenDeck;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.model.gameZone.MarbleMarket;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.utility.JsonUtil;

import java.util.ArrayList;

public class GameStateSerializer {
    private final Gson gson;
    private final String nickname;
    private final JsonObject message;

    public GameStateSerializer(String nickname) {
        this.gson = new Gson();
        this.message = new JsonObject();
        this.nickname = nickname;
    }

    public static JsonObject getJsonPlayerList() {
        ArrayList<Player> players = Game.getInstance().getPlayers();
        JsonObject jsonObject = new JsonObject();
        JsonArray playerList = new JsonArray();

        for (Player player : players) {
            Person person = (Person) player;

            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("nickname", person.getNickname());
            playerObject.addProperty("isHost", person.isHost());

            playerList.add(playerObject);
        }

        jsonObject.add("playerList", playerList);

        return jsonObject;
    }

    public JsonObject getMessage() {
        return message.deepCopy();
    }

    public void addPlayerDetails(Person person) {
        JsonObject player = new JsonObject();
        player.addProperty("nickname", person.getNickname());
        player.addProperty("inkwell", person.isActivePlayer());
        player.addProperty("initDiscard", !person.isConnected() || person.initDiscarded());
        player.addProperty("initResources", !person.isConnected() || person.initSelected());
        player.addProperty("mainAction", person.mainAction());

        if (!message.has("players")) {
            JsonArray players = new JsonArray();
            message.add("players", players);
        }
        message.getAsJsonArray("players").add(player);
    }

    public void addToPlayers(Person person, String property, JsonElement element) {
        addPlayerDetails(person);

        JsonArray players = message.getAsJsonArray("players");
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getAsJsonObject().get("nickname").getAsString().equals(person.getNickname())) {
                JsonObject player = players.get(i).getAsJsonObject();
                if (player.has("board")) {
                    player.get("board").getAsJsonObject().add(property, element);
                } else {
                    JsonObject board = new JsonObject();
                    board.add(property, element);
                    player.add("board", board);
                }

                return;
            }
        }
    }

    public JsonElement actionToken() {
        return JsonUtil.getInstance().serialize(ActionTokenDeck.getFlippedToken());
    }

    public void addFlippedActionToken() {
        if (!message.has("gameZone")) {
            JsonObject gameZone = new JsonObject();
            gameZone.add("actionToken", actionToken());
            message.add("gameZone", gameZone);
        } else {
            message.getAsJsonObject("gameZone").add("actionToken", actionToken());
        }
    }

    public void addMarbleMarket() {
        if (!message.has("gameZone")) {
            JsonObject gameZone = new JsonObject();
            gameZone.add("marbleMarket", marbleMarket());
            message.add("gameZone", gameZone);
        } else {
            message.getAsJsonObject("gameZone").add("marbleMarket", marbleMarket());
        }
    }

    public void addCardMarket() {
        if (!message.has("gameZone")) {
            JsonObject gameZone = new JsonObject();
            gameZone.add("cardMarket", cardMarket());
            message.add("gameZone", gameZone);
        } else {
            message.getAsJsonObject("gameZone").add("cardMarket", cardMarket());
        }
    }

    public void addFaithTrack(Person person) {
        addToPlayers(person, "faithTrack", faithTrack(person));
    }

    public void addWarehouse(Person person) {
        addToPlayers(person, "warehouse", warehouse(person));
    }

    public void addStrongbox(Person person) {
        String strongboxJson = gson.toJson(person.getBoard().getStrongBox().getResources());
        addToPlayers(person, "strongbox", JsonUtil.getInstance().parseLine(strongboxJson));
    }

    public void addDevCards(Person person) {
        addToPlayers(person, "devCards", devCards(person));
    }

    public void addPlayedLeaderCards(Person person) {
        addToPlayers(person, "playedLeaderCards", playedLeaderCards(person));
    }

    public void addHandLeaderCards(Person person) {
        addToPlayers(person, "handLeaderCards", handLeaderCards(person));
    }

    public void addBoard(Person person) {
        addPlayerDetails(person);

        JsonArray players = message.getAsJsonArray("players");
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getAsJsonObject().get("nickname").getAsString().equals(person.getNickname())) {
                players.get(i).getAsJsonObject().add("board", board(person));
            }
        }
    }

    public void addGameZone() {
        message.add("gameZone", gameZone());
    }

    public void addPlayers() {
        message.add("players", players());
    }

    public JsonObject marbleMarket() {
        JsonObject marbleMarket = new JsonObject();
        JsonArray table = new JsonArray();

        MarbleMarket marbleMarketObject = Game.getInstance().getGameZone().getMarbleMarket();

        for (int i = 0; i < marbleMarketObject.getRows(); ++i) {
            JsonArray row = new JsonArray();
            for (int j = 0; j < marbleMarketObject.getColumns(); ++j) {
                JsonElement jsonMarbleColour = JsonUtil.getInstance().parseLine(gson.toJson(marbleMarketObject.getMarketColour(i, j)));
                row.add(jsonMarbleColour);
            }
            table.add(row);
        }

        marbleMarket.add("market", table);
        JsonElement jsonMarbleColour = JsonUtil.getInstance().parseLine(gson.toJson(marbleMarketObject.getFreeMarbleColour()));
        marbleMarket.add("freeMarble", jsonMarbleColour);

        return marbleMarket;
    }

    public JsonObject cardMarket() {
        JsonObject marketObject = new JsonObject();
        JsonArray table = new JsonArray();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();

        for(int i = 0; i < CardLevel.values().length; ++i) {
            JsonArray row = new JsonArray();

            for(int j = 0; j < CardColour.values().length; ++j) {
                JsonObject deck = new JsonObject();

                if(cardMarket.size(i, j) == 0) {
                    deck.add("topCard", JsonNull.INSTANCE);
                } else {
                    deck.addProperty("topCard", gson.toJson(cardMarket.top(i, j).getId()));
                }
                deck.addProperty("quantity", gson.toJson(cardMarket.size(i, j)));

                row.add(deck);
            }

            table.add(row);
        }

        marketObject.add("market", table);
        return marketObject;
    }

    public JsonObject gameZone() {
        JsonObject gameZone = new JsonObject();
        gameZone.add("marbleMarket", marbleMarket());
        gameZone.add("cardMarket", cardMarket());
        return gameZone;
    }

    public JsonObject faithTrack(Person person) {
        JsonObject faithTrack = new JsonObject();
        int position = Integer.parseInt(gson.toJson(person.getBoard().getFaithTrack().getMarkerPosition()));
        JsonArray favors = new JsonArray();
        for (PopeFavor favor : person.getBoard().getFaithTrack().getReceivedPopeFavors()) {
            favors.add(favor.getId());
        }
        faithTrack.addProperty("position", position);
        faithTrack.add("receivedFavors", favors);

        if (Game.getInstance().getNumberOfPlayers() == 1) {
            faithTrack.addProperty("computerPosition", Game.getInstance().getComputer().getFaithTrack().getMarkerPosition());
        }

        return faithTrack;
    }

    public JsonArray warehouse(Person person) {
        JsonArray warehouse = new JsonArray();
        int size = person.getBoard().getWarehouse().numberOfDepots();
        for (int i = 0; i < size; i++) {
            String jsonString = gson.toJson(person.getBoard().getWarehouse().getDepotResources(i));
            warehouse.add(JsonUtil.getInstance().parseLine(jsonString));
        }

        return warehouse;
    }

    public JsonArray devCards(Person person) {
        JsonArray devCards = new JsonArray();

        for (int i = 0; i < BoardParser.getInstance().getDevelopmentCardsSlots(); ++i) {
            JsonArray cards = new JsonArray();

            DevCardDeck deck = person.getBoard().getDevelopmentCardArea().getDecks().get(i);
            for (DevCard devCard : deck.getCards()) {
                cards.add(devCard.getId());
            }

            devCards.add(cards);
        }

        return devCards;
    }

    public JsonArray playedLeaderCards(Person person) {
        JsonArray playedLeaderCards = new JsonArray();
        for (LeaderCard card : person.getBoard().getLeaderCardArea().getLeaderCards()) {
            if (card.isActive())
                playedLeaderCards.add(card.getId());
        }
        return playedLeaderCards;
    }

    public JsonArray handLeaderCards(Person person) {
        JsonArray handLeaderCards = new JsonArray();
        String nick = person.getNickname();
        for (LeaderCard card : person.getBoard().getLeaderCardArea().getLeaderCards()) {
            if (nickname.equals(nick)) {
                if (!card.isActive()) {
                    handLeaderCards.add(card.getId());
                }
            } else {
                handLeaderCards.add(JsonNull.INSTANCE);
            }
        }
        return handLeaderCards;
    }

    public JsonObject board(Person person) {
        JsonObject board = new JsonObject();
        board.add("faithTrack", faithTrack(person));
        board.add("warehouse", warehouse(person));
        String strongboxJson = gson.toJson(person.getBoard().getStrongBox().getResources());
        board.add("strongbox", JsonUtil.getInstance().parseLine(strongboxJson));
        board.add("devCards", devCards(person));
        board.add("playedLeaderCards", playedLeaderCards(person));
        board.add("handLeaderCards", handLeaderCards(person));
        return board;
    }

    public JsonArray players() {
        int i = 0;
        JsonArray players = new JsonArray();
        for (Player player : Game.getInstance().getPlayers()) {
            if (player.getPlayerType() == PlayerType.PERSON) {
                Person person = (Person) player;
                JsonObject playerObject = new JsonObject();
                playerObject.addProperty("nickname", person.getNickname());
                playerObject.addProperty("inkwell", person.isActivePlayer());
                playerObject.addProperty("initDiscard", !person.isConnected() || person.initDiscarded());
                playerObject.addProperty("initResources", !person.isConnected() || person.initSelected());
                playerObject.addProperty("mainAction", person.mainAction());
                playerObject.add("board", board(person));
                players.add(playerObject);
            }
        }
        return players;
    }

    public JsonObject gameState() {
        JsonObject gameState = new JsonObject();
        gameState.add("gameZone", gameZone());
        gameState.add("players", players());

        return gameState;
    }
}
