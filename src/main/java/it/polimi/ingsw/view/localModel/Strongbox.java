package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

public class Strongbox extends LocalModelElement {
    private ConcreteResourceSet content;

    @Override
    public void updateModel(JsonElement strongboxJson) {
        content = gson.fromJson(strongboxJson, ConcreteResourceSet.class);
        notifyObservers();
    }

    public ConcreteResourceSet getContent() {
        return content;
    }
}
