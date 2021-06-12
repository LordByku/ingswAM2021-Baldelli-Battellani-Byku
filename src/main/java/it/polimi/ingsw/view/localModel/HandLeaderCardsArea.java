package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;

import java.util.ArrayList;

public class HandLeaderCardsArea extends LocalModelElement {
    private ArrayList<Integer> leaderCards;

    @Override
    public void updateModel(JsonElement leaderCardsJson) {
        notifyObservers();
    }

    public ArrayList<Integer> getLeaderCards() {
        return leaderCards;
    }
}
