package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;

public class Player implements LocalModelElement {
    private String nickname;
    private boolean inkwell;
    private boolean initDiscard;
    private boolean initResources;
    private boolean mainAction;
    private Board board;
    private CommandBuffer commandBuffer;

    public String getNickname() {
        return nickname;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void updateModel(JsonObject playerJson) {
        inkwell = playerJson.get("inkwell").getAsBoolean();
        initDiscard = playerJson.get("initDiscard").getAsBoolean();
        initResources = playerJson.get("initResources").getAsBoolean();
        mainAction = playerJson.get("mainAction").getAsBoolean();
        if(playerJson.has("board")) {
            board.updateModel(playerJson.getAsJsonObject("board"));
        }
    }

    public void setCommandBuffer(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public boolean hasInkwell() {
        return inkwell;
    }

    public boolean initDiscard() {
        return initDiscard;
    }

    public boolean initResources() {
        return initResources;
    }

    public boolean mainAction() {
        return mainAction;
    }
}
