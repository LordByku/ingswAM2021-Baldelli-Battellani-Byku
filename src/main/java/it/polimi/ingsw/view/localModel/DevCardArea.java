package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;

import java.util.ArrayList;

public class DevCardArea extends LocalModelElement {
    private ArrayList<ArrayList<Integer>> decks;

    @Override
    public void updateModel(JsonElement devCardsJson) {
        notifyObservers();
    }

    public ArrayList<ArrayList<Integer>> getDecks() {
        return decks;
    }
}
