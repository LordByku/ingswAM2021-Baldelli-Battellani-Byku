package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards;

import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.Effect;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.Requirements;

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
}
