package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.devCards.CardTypeDetails;
import it.polimi.ingsw.model.devCards.CardTypeSet;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardRequirements;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;

public class Player extends LocalModelElement {
    private final CommandElement command = new CommandElement();
    private String nickname;
    private boolean inkwell;
    private boolean initDiscard;
    private boolean initResources;
    private boolean mainAction;
    private Board board;

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

    public boolean canPlay(LocalModel model, LeaderCardRequirements leaderCardRequirements) {
        if (model.allInitDiscard() && model.allInitResources() && hasInkwell() ) {
            switch (leaderCardRequirements.getRequirementsType()) {
                case CARDSET: {
                    ArrayList<ArrayList<Integer>> decks = board.getDevCardsArea().getDecks();
                    CardTypeSet cardTypeSet = (CardTypeSet) leaderCardRequirements;

                    for (CardTypeDetails cardTypeDetails : cardTypeSet.getCardTypes().values()) {
                        int satisfiedCount = 0;
                        for(ArrayList<Integer> deck: decks) {
                            for(Integer cardId: deck) {
                                DevCard card = DevCardsParser.getInstance().getCard(cardId);
                                if (cardTypeDetails.isSatisfied(card)) {
                                    satisfiedCount++;
                                }
                            }
                        }
                        if (satisfiedCount < cardTypeDetails.getQuantity()) {
                            return false;
                        }
                    }

                    return true;
                }
                case RESOURCES: {
                    ConcreteResourceSet requirements = (ConcreteResourceSet) leaderCardRequirements;
                    ConcreteResourceSet resources = board.getResources();
                    return resources.contains(requirements);
                }
                default: {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean canMainAction(LocalModel model) {
        return model.allInitDiscard() && model.allInitResources() && hasInkwell() && !mainAction();
    }

    public boolean canEndTurn(LocalModel model) {
        return model.allInitDiscard() && model.allInitResources() && hasInkwell() && mainAction();
    }
}
