package it.polimi.ingsw.editor.model.simplifiedModel;

import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import java.util.ResourceBundle;

public class DevCard {
    private CardColour colour;
    private CardLevel level;
    private int points;
    private ConcreteResourceSet requirements;
    private SpendableResourceSet productionIn;
    private ObtainableResourceSet productionOut;

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

    public CardLevel getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
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


    public void setLevel(CardLevel level) {
        this.level = level;
    }

    public void setColour(CardColour colour) {
        this.colour = colour;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
