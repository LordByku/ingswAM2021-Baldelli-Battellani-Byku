package it.polimi.ingsw.view.localModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface LocalModelElement {
    Gson gson = new Gson();

    void updateModel(JsonObject jsonObject);
}
