package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;

public class PlayedLeaderCardsArea extends LocalModelElement {
    private ArrayList<Integer> leaderCards;

    @Override
    public void updateModel(JsonElement leaderCardsJson) {
        leaderCards.clear();
        for (JsonElement leaderCardId : leaderCardsJson.getAsJsonObject().getAsJsonArray("leaderCards")) {
            leaderCards.add(leaderCardId.getAsInt());
        }
        notifyObservers(NotificationSource.PLAYEDLEADERCARDSAREA);
    }

    public ArrayList<Integer> getLeaderCards() {
        return leaderCards;
    }
}
