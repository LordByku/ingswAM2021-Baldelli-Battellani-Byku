package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.actionTokens.ActionToken;

public class ActionTokenDeck extends LocalModelElement {
    private ActionToken flippedActionToken;

    @Override
    public void updateModel(JsonElement jsonElement) {
        notifyObservers();
    }

    public ActionToken getFlippedActionToken() {
        return flippedActionToken;
    }

    public void resetActionToken() {
        flippedActionToken = null;
    }
}
