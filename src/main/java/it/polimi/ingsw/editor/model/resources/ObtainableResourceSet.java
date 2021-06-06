package it.polimi.ingsw.editor.model.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utility.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class ObtainableResourceSet {
    private final HashMap<ObtainableResource, Integer> resources;

    public ObtainableResourceSet() {
        resources = new HashMap<>();
    }

    public void parse(JsonArray jsonArray) {
        Gson gson = new Gson();

        for(JsonElement jsonElement: jsonArray) {
            JsonObject resource = jsonElement.getAsJsonObject();

            ObtainableResource obtainableResource = gson.fromJson(resource.get("resource"), ObtainableResource.class);
            int quantity = resource.get("quantity").getAsInt();

            resources.put(obtainableResource, quantity);
        }
    }

    public int getQuantity(ObtainableResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    public void updateQuantity(ObtainableResource resource, int value) {
        resources.put(resource, value);
    }

    public JsonArray serialize() {
        JsonArray array = new JsonArray();

        for(Map.Entry<ObtainableResource, Integer> entry: resources.entrySet()) {
            ObtainableResource resource = entry.getKey();
            int quantity = entry.getValue();

            if(quantity > 0) {
                JsonObject resourceObject = new JsonObject();
                resourceObject.add("resource", JsonUtil.getInstance().serialize(resource));
                resourceObject.addProperty("quantity", quantity);

                array.add(resourceObject);
            }
        }

        return array;
    }
}
