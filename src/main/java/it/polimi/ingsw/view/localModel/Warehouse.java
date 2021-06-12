package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

public class Warehouse extends LocalModelElement {
    private ArrayList<ConcreteResourceSet> depots;

    @Override
    public void updateModel(JsonElement warehouseJson) {
        notifyObservers();
    }

    public ArrayList<ConcreteResourceSet> getDepots() {
        return depots;
    }
}
