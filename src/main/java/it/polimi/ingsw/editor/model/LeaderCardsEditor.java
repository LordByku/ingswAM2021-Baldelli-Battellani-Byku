package it.polimi.ingsw.editor.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.LeaderCard;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.DiscountEffect;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.Effect;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.EffectType;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.RequirementType;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.Requirements;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.ResourcesRequirements;

import java.util.ArrayList;

public class LeaderCardsEditor {
    private final ArrayList<LeaderCard> leaderCards;
    private int currentSelection;

    public LeaderCardsEditor(JsonArray jsonArray) {
        Gson gson = new Gson();

        currentSelection = 0;
        leaderCards = new ArrayList<>();
        for(JsonElement jsonElement: jsonArray) {
            JsonObject cardObject = jsonElement.getAsJsonObject();

            int points = cardObject.get("points").getAsInt();
            RequirementType requirementType = gson.fromJson(cardObject.get("requirementsType"), RequirementType.class);
            Requirements requirements = requirementType.buildRequirements(cardObject.getAsJsonArray("requirements"));
            JsonObject effectObject = cardObject.getAsJsonObject("effect");
            EffectType effectType = gson.fromJson(effectObject.get("effectType"), EffectType.class);
            Effect effect = effectType.getEffect(effectObject);

            leaderCards.add(new LeaderCard(points, requirements, effect));
        }
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(int currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void addNewCard() {
        leaderCards.add(new LeaderCard(1, new ResourcesRequirements(), new DiscountEffect()));
    }

    public void removeCard(int index) {
        leaderCards.remove(index);
    }
}
