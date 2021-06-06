package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards;

import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.Effect;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.Requirements;
import it.polimi.ingsw.utility.JsonUtil;

public class LeaderCard {
    private int points;
    private Requirements requirements;
    private Effect effect;

    public LeaderCard(int points, Requirements requirements, Effect effect) {
        this.points = points;
        this.requirements = requirements;
        this.effect = effect;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        json.addProperty("points", points);
        json.add("requirementsType", JsonUtil.getInstance().serialize(requirements.getRequirementType()));
        json.add("requirements", requirements.serialize());
        json.add("effect", effect.serialize());

        return json;
    }
}
