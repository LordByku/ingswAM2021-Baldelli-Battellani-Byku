package it.polimi.ingsw.network.client.localModel;

import com.google.gson.JsonObject;

public class Player implements LocalModelElement {
    private String nickname;
    private boolean inkwell;
    private Board board;

    public String getNickname() {
        return nickname;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void updateModel(JsonObject playerJson) {
        inkwell = playerJson.get("inkwell").getAsBoolean();
        if(playerJson.has("board")) {
            board.updateModel(playerJson.getAsJsonObject("board"));
        }
    }
}
