package it.polimi.ingsw.utility;

import com.google.gson.*;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.view.localModel.LocalModel;

public class Deserializer {
    private static Deserializer instance;
    private final Gson gson;

    private Deserializer() {
        gson = new Gson();
    }

    public static Deserializer getInstance() {
        if(instance == null) {
            instance = new Deserializer();
        }
        return instance;
    }

    public int[] getIntArray(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, int[].class);
    }

    public ConcreteResource[] getConcreteResourceArray(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, ConcreteResource[].class);
    }

    public ConcreteResourceSet[] getConcreteResourceSetArray(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, ConcreteResourceSet[].class);
    }

    public ConcreteResource getConcreteResource(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, ConcreteResource.class);
    }

    public ConcreteResourceSet getConcreteResourceSet(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, ConcreteResourceSet.class);
    }

    public CommandType getCommandType(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, CommandType.class);
    }

    public CommandBuffer getCommandBuffer(JsonElement jsonElement) {
        if(jsonElement == JsonNull.INSTANCE) {
            return null;
        }
        CommandType commandType = getCommandType(jsonElement.getAsJsonObject().get("commandType"));
        return gson.fromJson(jsonElement, commandType.getCommandBufferClass());
    }

    public LocalModel getLocalModel(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, LocalModel.class);
    }

    public CheckPoint getCheckPoint(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, CheckPoint.class);
    }

}
