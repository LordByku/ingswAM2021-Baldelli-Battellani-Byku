package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.game.*;
import com.google.gson.*;
import it.polimi.ingsw.model.gameZone.MarbleMarket;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;

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

    public JsonObject getMessage(){
        return message.deepCopy();
    }

    public void addPlayerDetails(Person person) {
        JsonObject player = new JsonObject();
        player.addProperty("nickname", person.getNickname());
        player.addProperty("inkwell", person.isActivePlayer());

        if(!message.has("players")) {
            JsonArray players = new JsonArray();
            message.add("players", players);
        }
        message.getAsJsonArray("players").add(player);
    }

    public void addToPlayers(Person person, String property, JsonElement element){
        addPlayerDetails(person);

        JsonArray players = message.getAsJsonArray("players");
        for(int i=0; i<players.size(); i++) {
            if (players.get(i).getAsJsonObject().get("nickname").getAsString().equals(person.getNickname())) {
                JsonObject player = players.get(i).getAsJsonObject();
                if (player.has("board")) {
                    player.get("board").getAsJsonObject().add(property,element);
                } else {
                    JsonObject board = new JsonObject();
                    board.add(property,element);
                    player.add("board", board);
                }

                return;
            }
        }
    }

    public void addMarbleMarket(){
        if(!message.has("gameZone")){
            JsonObject gameZone = new JsonObject();
            gameZone.add("marbleMarket",marbleMarket());
            message.add("gameZone", gameZone);
        }
        else {
            message.getAsJsonObject("gameZone").add("marbleMarket",marbleMarket());
        }
    }

    public void addCardMarket() {
        if (!message.has("gameZone")) {
            JsonObject gameZone = new JsonObject();
            gameZone.add("cardMarket", cardMarket());
            message.add("gameZone", gameZone);
        }
        else{
            message.getAsJsonObject("gameZone").add("cardMarket", cardMarket());
        }
    }

    public void addFaithTrack(Person person){
        addToPlayers(person,"faithTrack",faithTrack(person));
    }

    public void addWarehouse(Person person){
        addToPlayers(person,"warehouse",warehouse(person));
    }

    public void addStrongbox(Person person){
        String strongboxJson = gson.toJson(person.getBoard().getStrongBox().getResources());
        addToPlayers(person,"strongbox", ServerParser.getInstance().parseLine(strongboxJson));
    }

    public void addDevCards(Person person){
        addToPlayers(person, "devCards", devCards(person));
    }

    public void addPlayedLeaderCards(Person person){
        addToPlayers(person,"playedLeaderCards",playedLeaderCards(person));
    }

    public void addHandLeaderCards(Person person){
        addToPlayers(person,"handLeaderCards",handLeaderCards(person));
    }

    public void addBoard(Person person){
        addPlayerDetails(person);

        JsonArray players = message.getAsJsonArray("players");
        for(int i=0; i<players.size(); i++){
            if(players.get(i).getAsJsonObject().get("nickname").getAsString().equals(person.getNickname())){
                players.get(i).getAsJsonObject().add("board", board(person));
            }
        }
    }

    public void addGameZone(){
        message.add("gameZone", gameZone());
    }

    public void addPlayers(){
        message.add("players",players());
    }

    public JsonObject marbleMarket(){
        JsonObject marbleMarket = new JsonObject();
        JsonArray table = new JsonArray();

        MarbleMarket marbleMarketObject = Game.getInstance().getGameZone().getMarbleMarket();

        for(int i = 0; i < marbleMarketObject.getRows(); ++i) {
            JsonArray row = new JsonArray();
            for(int j = 0; j < marbleMarketObject.getColumns(); ++j) {
                JsonElement jsonMarbleColour = ServerParser.getInstance().parseLine(gson.toJson(marbleMarketObject.getMarketColour(i, j)));
                row.add(jsonMarbleColour);
            }
            table.add(row);
        }

        marbleMarket.add("market", table);
        JsonElement jsonMarbleColour = ServerParser.getInstance().parseLine(gson.toJson(marbleMarketObject.getFreeMarbleColour()));
        marbleMarket.add("freeMarble", jsonMarbleColour);

        return marbleMarket;
    }

    public JsonObject cardMarket(){
        JsonObject cardMarket = new JsonObject();
        JsonArray table = new JsonArray();
        JsonArray cardRows1 = new JsonArray();
        JsonArray cardRows2 = new JsonArray();
        JsonArray cardRows3 = new JsonArray();
        for(int i=0; i<4; i++){
            JsonObject cardDeck = new JsonObject();
            cardDeck.addProperty("topCard",gson.toJson(Game.getInstance().getGameZone().getCardMarket().top(0,i).getId()));
            cardDeck.addProperty("quantity",gson.toJson(Game.getInstance().getGameZone().getCardMarket().size(0,i)));
            cardRows1.add(cardDeck);
        }
        for(int i=0; i<4; i++){
            JsonObject cardDeck = new JsonObject();
            cardDeck.addProperty("topCard",gson.toJson(Game.getInstance().getGameZone().getCardMarket().top(1,i).getId()));
            cardDeck.addProperty("quantity",gson.toJson(Game.getInstance().getGameZone().getCardMarket().size(1,i)));
            cardRows2.add(cardDeck);
        }
        for(int i=0; i<4; i++){
            JsonObject cardDeck = new JsonObject();
            cardDeck.addProperty("topCard",gson.toJson(Game.getInstance().getGameZone().getCardMarket().top(2,i).getId()));
            cardDeck.addProperty("quantity",gson.toJson(Game.getInstance().getGameZone().getCardMarket().size(2,i)));
            cardRows3.add(cardDeck);
        }
        table.add(cardRows1);
        table.add(cardRows2);
        table.add(cardRows3);

        cardMarket.add("market", table);
        return cardMarket;
    }

    public JsonObject gameZone(){
        JsonObject gameZone = new JsonObject();
        gameZone.add("marbleMarket", marbleMarket());
        gameZone.add("cardMarket", cardMarket());
        return gameZone;
    }

    public JsonObject faithTrack(Person person) {
        JsonObject faithTrack = new JsonObject();
        int position = Integer.parseInt(gson.toJson(person.getBoard().getFaithTrack().getMarkerPosition()));
        JsonArray favors = new JsonArray();
        for(PopeFavor favor: person.getBoard().getFaithTrack().getReceivedPopeFavors()){
            favors.add(favor.getId());
        }
        faithTrack.addProperty("position", position);
        faithTrack.add("receivedFavors", favors);

        if(Game.getInstance().getNumberOfPlayers() == 1) {
            faithTrack.addProperty("computerPosition", Game.getInstance().getComputer().getFaithTrack().getMarkerPosition());
        }

        return faithTrack;
    }

    public JsonArray warehouse(Person person){
        JsonArray warehouse = new JsonArray();
        int size = person.getBoard().getWarehouse().numberOfDepots();
        for(int i =0; i < size; i++){
            String jsonString = gson.toJson(person.getBoard().getWarehouse().getDepotResources(i));
            warehouse.add(ServerParser.getInstance().parseLine(jsonString));
        }

        return warehouse;
    }

    public JsonArray devCards(Person person){
        JsonArray devCards = new JsonArray();
        JsonArray devCardDeck1 = new JsonArray();
        JsonArray devCardDeck2 = new JsonArray();
        JsonArray devCardDeck3 = new JsonArray();

        DevCardDeck deck1 = person.getBoard().getDevelopmentCardArea().getDecks().get(0);
        DevCardDeck deck2 = person.getBoard().getDevelopmentCardArea().getDecks().get(1);
        DevCardDeck deck3 = person.getBoard().getDevelopmentCardArea().getDecks().get(2);

        for (DevCard card: deck1.getCards()){
            devCardDeck1.add(card.getId());
        }
        for (DevCard card: deck2.getCards()){
            devCardDeck2.add(card.getId());
        }
        for (DevCard card: deck3.getCards()){
            devCardDeck3.add(card.getId());
        }

        devCards.add(devCardDeck1);
        devCards.add(devCardDeck2);
        devCards.add(devCardDeck3);

        return devCards;
    }

    public JsonArray playedLeaderCards(Person person){
        JsonArray playedLeaderCards = new JsonArray();
        for(LeaderCard card: person.getBoard().getLeaderCardArea().getLeaderCards()){
            if(card.isActive())
                playedLeaderCards.add(card.getId());
        }
        return playedLeaderCards;
    }

    public JsonArray handLeaderCards(Person person){
        JsonArray handLeaderCards = new JsonArray();
        String nick = person.getNickname();
        for(LeaderCard card: person.getBoard().getLeaderCardArea().getLeaderCards()){
            if(nickname.equals(nick)) {
                if (!card.isActive()) {
                    handLeaderCards.add(card.getId());
                }
            } else {
                handLeaderCards.add(JsonNull.INSTANCE);
            }
        }
        return handLeaderCards;
    }

    public JsonObject board(Person person){
        JsonObject board = new JsonObject();
        board.add("faithTrack", faithTrack(person));
        board.add("warehouse", warehouse(person));
        String strongboxJson = gson.toJson(person.getBoard().getStrongBox().getResources());
        board.add("strongbox", ServerParser.getInstance().parseLine(strongboxJson));
        board.add("devCards", devCards(person));
        board.add("playedLeaderCards", playedLeaderCards(person));
        board.add("handLeaderCards", handLeaderCards(person));
        return board;
    }

    public JsonArray players() {
        int i = 0;
        JsonArray players = new JsonArray();
        for(Player player: Game.getInstance().getPlayers()) {
            if(player.getPlayerType() == PlayerType.PERSON) {
                Person person = (Person) player;
                JsonObject object = new JsonObject();
                object.addProperty("nickname", person.getNickname());
                boolean inkwell = (i == 0);
                object.addProperty("inkwell", inkwell);
                object.add("board", board(person));
                i++;

                players.add(object);
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
