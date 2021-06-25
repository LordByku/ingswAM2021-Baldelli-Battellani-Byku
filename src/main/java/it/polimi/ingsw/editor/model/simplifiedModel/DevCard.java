package it.polimi.ingsw.editor.model.simplifiedModel;

import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.utility.JsonUtil;

public class DevCard {
    private CardColour colour;
    private CardLevel level;
    private int points;
    private final ConcreteResourceSet requirements;
    private final SpendableResourceSet productionIn;
    private final ObtainableResourceSet productionOut;

    public DevCard(CardColour colour, CardLevel level, int points, ConcreteResourceSet requirements,
                   SpendableResourceSet productionIn, ObtainableResourceSet productionOut) {
        this.colour = colour;
        this.level = level;
        this.points = points;
        this.requirements = requirements;
        this.productionIn = productionIn;
        this.productionOut = productionOut;
    }

    public CardColour getColour() {
        return colour;
    }

    public void setColour(CardColour colour) {
        this.colour = colour;
    }

    public CardLevel getLevel() {
        return level;
    }

    public void setLevel(CardLevel level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ConcreteResourceSet getRequirements() {
        return requirements;
    }

    public SpendableResourceSet getProductionIn() {
        return productionIn;
    }

    public ObtainableResourceSet getProductionOut() {
        return productionOut;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        json.add("colour", JsonUtil.getInstance().serialize(colour));
        json.add("level", JsonUtil.getInstance().serialize(level));
        json.addProperty("points", points);
        json.add("requirements", requirements.serialize());
        json.add("productionPower", Config.writeProductionPower(productionIn, productionOut));

        return json;
    }
}
