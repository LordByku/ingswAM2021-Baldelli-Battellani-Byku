package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

public class Player extends LocalModelElement {
    private String nickname;
    private boolean inkwell;
    private boolean initDiscard;
    private boolean initResources;
    private boolean mainAction;
    private Board board;
    private final CommandElement command = new CommandElement();

    public String getNickname() {
        return nickname;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void updateModel(JsonElement playerJson) {
        JsonObject playerObject = playerJson.getAsJsonObject();
        inkwell = playerObject.get("inkwell").getAsBoolean();
        initDiscard = playerObject.get("initDiscard").getAsBoolean();
        initResources = playerObject.get("initResources").getAsBoolean();
        mainAction = playerObject.get("mainAction").getAsBoolean();
        if (playerObject.has("board")) {
            board.updateModel(playerObject.getAsJsonObject("board"));
        }

        notifyObservers(NotificationSource.PLAYER);
    }

    public CommandBuffer getCommandBuffer() {
        return command.getCommandBuffer();
    }

    public void setCommandBuffer(CommandBuffer commandBuffer) {
        command.updateCommand(commandBuffer);
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

    public CommandElement getCommandElement() {
        return command;
    }

    public boolean canDiscard(LocalModel model) {
        if (!initDiscard) {
            return true;
        }
        return model.allInitDiscard() && model.allInitResources() && hasInkwell();
    }

    public boolean canPlay(LocalModel model) {
        return model.allInitDiscard() && model.allInitResources() && hasInkwell();
    }

    public boolean canMainAction(LocalModel model) {
        return model.allInitDiscard() && model.allInitResources() && hasInkwell() && !mainAction();
    }

    public boolean canEndTurn(LocalModel model) {
        return model.allInitDiscard() && model.allInitResources() && hasInkwell() && mainAction();
    }
}
