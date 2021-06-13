package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

public class Warehouse extends LocalModelElement {
    private ArrayList<ConcreteResourceSet> depots;

    @Override
    public void updateModel(JsonElement warehouseJson) {
        depots.clear();
        for (JsonElement depotJson : warehouseJson.getAsJsonObject().getAsJsonArray("depots")) {
            depots.add(gson.fromJson(depotJson, ConcreteResourceSet.class));
        }
        notifyObservers();
    }

    public ArrayList<ConcreteResourceSet> getDepots() {
        return depots;
    }
}
