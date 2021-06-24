package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

public class Strongbox extends LocalModelElement {
    private ConcreteResourceSet content;

    @Override
    public void updateModel(JsonElement strongboxJson) {
        content = gson.fromJson(strongboxJson.getAsJsonObject().get("content"), ConcreteResourceSet.class);
        notifyObservers(NotificationSource.STRONGBOX);
    }

    public ConcreteResourceSet getContent() {
        return content;
    }
}
