package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardDeck;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import com.google.gson.*;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.faithTrack.PopeFavor;

public class GameStateSerializer {
    private final Gson gson;
    private final String nickname;
    private final JsonObject message;

    public GameStateSerializer(String nickname) {
        this.gson = new Gson();
        message=new JsonObject();
        this.nickname = nickname;
    }

    public JsonObject getMessage(){
        return new Gson().fromJson(gson.toJson(message, JsonObject.class), JsonObject.class);
    }

    public void addMarbleMarket(){
        message.add("marbleMarket",marbleMarket());
    }
    public void addCardMarket(){
        message.add("cardMarket",cardMarket());
    }
    public void addGameZone(){
        message.add("gameZone", gameZone());
    }
    public void addFaithTrack(Player player){
        message.add("faithTrack",faithTrack(player));
    }

    public void addWarehouse(Player player){
        message.add("warehouse",warehouse(player));
    }
    public void addDevCards(Player player){
        message.add("devCards",devCards(player));
    }
    public void addPlayedLeaderCards(Player player){
        message.add("playedLeaderCards",playedLeaderCards(player));
    }
    public void addHandLeaderCards(Player player){
        message.add("handLeaderCards",handLeaderCards(player));
    }
    public void addBoard(Player player){
        message.add("board",board(player));
    }
    public void addPlayers(){
        message.add("players",players());
    }

    public JsonObject marbleMarket(){
        JsonObject marbleMarket = new JsonObject();
        JsonArray table = new JsonArray();
        JsonArray colorRows1 = new JsonArray();
        JsonArray colorRows2 = new JsonArray();
        JsonArray colorRows3 = new JsonArray();
        for(int i=0; i<4; i++){
            colorRows1.add(gson.toJson(Game.getInstance().getGameZone().getMarbleMarket().getMarketColour(0, i)));
        }
        for(int i=0; i<4; i++){
            colorRows2.add(gson.toJson(Game.getInstance().getGameZone().getMarbleMarket().getMarketColour(1, i)));
        }
        for(int i=0; i<4; i++){
            colorRows3.add(gson.toJson(Game.getInstance().getGameZone().getMarbleMarket().getMarketColour(2, i)));
        }
        table.add(colorRows1);
        table.add(colorRows2);
        table.add(colorRows3);

        marbleMarket.add("market", table);
        String freeMarble = gson.toJson(Game.getInstance().getGameZone().getMarbleMarket().getFreeMarbleColour());
        marbleMarket.addProperty("freeMarble", freeMarble);

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

    public JsonObject faithTrack(Player player){
        JsonObject faithTrack = new JsonObject();
        int position = Integer.parseInt(gson.toJson(((Person) player).getBoard().getFaithTrack().getMarkerPosition()));
        JsonArray favors = new JsonArray();
        for(PopeFavor favor: ((Person) player).getBoard().getFaithTrack().getReceivedPopeFavors()){
            favors.add(favor.getId());
        }
        faithTrack.addProperty("position", position);
        faithTrack.add("receivedFavors", favors);

        return  faithTrack;
    }

    public JsonArray warehouse(Player player){
        JsonArray warehouse = new JsonArray();
        int size = ((Person) player).getBoard().getWarehouse().numberOfDepots();
        for(int i =0; i < size; i++){
            String jsonString = gson.toJson(((Person) player).getBoard().getWarehouse().getDepotResources(i));
            warehouse.add(ServerParser.getInstance().parseLine(jsonString));
        }

        return warehouse;
    }

    public JsonArray devCards(Player player){
        JsonArray devCards = new JsonArray();
        JsonArray devCardDeck1 = new JsonArray();
        JsonArray devCardDeck2 = new JsonArray();
        JsonArray devCardDeck3 = new JsonArray();

        DevCardDeck deck1 = ((Person) player).getBoard().getDevelopmentCardArea().getDecks().get(0);
        DevCardDeck deck2 = ((Person) player).getBoard().getDevelopmentCardArea().getDecks().get(1);
        DevCardDeck deck3 = ((Person) player).getBoard().getDevelopmentCardArea().getDecks().get(2);

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

    public JsonArray playedLeaderCards(Player player){
        JsonArray playedLeaderCards = new JsonArray();
        for(LeaderCard card: ((Person) player).getBoard().getLeaderCardArea().getLeaderCards()){
            if(card.isActive())
                playedLeaderCards.add(card.getId());
        }
        return playedLeaderCards;
    }

    public JsonArray handLeaderCards(Player player){
        JsonArray handLeaderCards = new JsonArray();
        String nick = ((Person) player).getNickname();
        for(LeaderCard card: ((Person) player).getBoard().getLeaderCardArea().getLeaderCards()){
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

    public JsonObject board(Player player){
        JsonObject board = new JsonObject();
        board.add("faithTrack", faithTrack(player));
        board.add("warehouse", warehouse(player));
        String strongboxJson = gson.toJson(((Person) player).getBoard().getStrongBox().getResources());
        board.add("strongbox", ServerParser.getInstance().parseLine(strongboxJson));
        board.add("devCards", devCards(player));
        board.add("playedLeaderCards", playedLeaderCards(player));
        board.add("handLeaderCards", handLeaderCards(player));
        return board;
    }

    public JsonArray players(){
        int i=0;
        JsonArray players = new JsonArray();
        for(Player player: Game.getInstance().getPlayers()){
            JsonObject object = new JsonObject();
            object.addProperty("nickname", ((Person) player).getNickname());
            boolean inkwell = (i==0);
            object.addProperty("inkwell", inkwell);
            object.add("board", board(player));
            i++;

            players.add(object);
        }
        return players;
    }

    public JsonObject gameState(){
        JsonObject gameState = new JsonObject();
        gameState.add("gameZone", gameZone());
        gameState.add("players", players());

        return gameState;
    }
}
