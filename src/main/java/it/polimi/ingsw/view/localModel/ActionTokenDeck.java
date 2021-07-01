package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.actionTokens.ActionToken;

import static it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

public class ActionTokenDeck extends LocalModelElement {
    private ActionToken flippedActionToken = null;

    @Override
    public void updateModel(JsonElement actionTokenDeckJson) {
        flippedActionToken = gson.fromJson(actionTokenDeckJson.getAsJsonObject().get("flippedActionToken"), ActionToken.class);
        notifyObservers(NotificationSource.ACTIONTOKENDECK);
    }

    public ActionToken getFlippedActionToken() {
        return flippedActionToken;
    }

    public void resetActionToken() {
        flippedActionToken = null;
    }
}
