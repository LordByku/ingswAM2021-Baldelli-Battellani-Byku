package it.polimi.ingsw.editor.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.editor.model.simplifiedModel.DevCard;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import java.util.ArrayList;

public class DevCardsEditor {
    private final ArrayList<DevCard> devCards;
    private int currentSelection;

    public DevCardsEditor(JsonArray jsonArray) {
        Gson gson = new Gson();

        currentSelection = 0;
        devCards = new ArrayList<>();
        for (JsonElement jsonElement: jsonArray) {
            JsonObject cardObject = jsonElement.getAsJsonObject();
            CardColour cardColour = gson.fromJson(cardObject.get("colour"), CardColour.class);
            CardLevel cardLevel = gson.fromJson(cardObject.get("level"), CardLevel.class);
            int points = cardObject.get("points").getAsInt();
            ConcreteResourceSet concrete = new ConcreteResourceSet();
            concrete.parse(cardObject.getAsJsonArray("requirements"));
            SpendableResourceSet spendable = new SpendableResourceSet();
            spendable.parse(cardObject.getAsJsonObject("productionPower").getAsJsonArray("productionIn"));
            ObtainableResourceSet obtainable = new ObtainableResourceSet();
            obtainable.parse(cardObject.getAsJsonObject("productionPower").getAsJsonArray("productionOut"));

            devCards.add(new DevCard(cardColour, cardLevel, points, concrete, spendable, obtainable));
        }
    }

    public ArrayList<DevCard> getDevCards() {
        return devCards;
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(int currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void addNewCard() {
        devCards.add(new DevCard(CardColour.GREEN, CardLevel.I, 1,
                new ConcreteResourceSet(), new SpendableResourceSet(), new ObtainableResourceSet()));
    }

    public void removeCard(int index) {
        devCards.remove(index);
    }
}
