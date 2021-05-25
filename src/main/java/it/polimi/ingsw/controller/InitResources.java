package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.parsing.InitGameParser;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.HashSet;
import java.util.function.Consumer;

public class InitResources extends CommandBuffer {
    private ConcreteResourceSet[] resources;

    protected InitResources(CommandType commandType, Person person) {
        super(commandType, person);

        if (person.initSelected() && initDiscardsMissing()) {
            throw new InvalidCommandException();
        }

        resources = null;
    }

    @Override
    public boolean isReady() {
        return resources != null;
    }

    @Override
    public Consumer<GameStateSerializer> complete() throws InvalidCommandException {
        if (!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();

        for (int i = 0; i < resources.length; ++i) {
            warehouse.addResources(i, resources[i]);
        }

        person.initSelectDone();

        setCompleted();

        return (serializer) -> {
            serializer.addWarehouse(person);
        };
    }

    @Override
    public Consumer<GameStateSerializer> cancel() {
        return (serializer) -> {
        };
    }

    @Override
    public Consumer<GameStateSerializer> handleMessage(String command, JsonElement value) throws RuntimeException {
        if (command.equals("resources")) {
            JsonArray jsonArray = value.getAsJsonArray();
            ConcreteResourceSet[] resources = Deserializer.getInstance().getConcreteResourceSetArray(jsonArray);
            if (resources != null) {
                setResources(resources);
            }
        }

        if (isReady()) {
            return complete();
        } else {
            return null;
        }
    }

    private void setResources(ConcreteResourceSet[] resources) {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();

        if (resources.length != warehouse.numberOfDepots()) {
            return;
        }

        HashSet<ConcreteResource> resourceTypes = new HashSet<>();
        int totalSize = 0;
        for (int i = 0; i < resources.length; ++i) {
            if (resources[i].hasMultipleTypes()) {
                return;
            }
            if (!warehouse.canAdd(i, resources[i])) {
                return;
            }
            ConcreteResource resourceType = resources[i].getResourceType();
            if (resourceType != null) {
                if (resourceTypes.contains(resourceType)) {
                    return;
                }
                resourceTypes.add(resources[i].getResourceType());
            }
            totalSize += resources[i].size();
        }

        int playerIndex = Game.getInstance().getPlayerIndex(person);
        int resourcesToReceive = InitGameParser.getInstance().getInitResources(playerIndex);

        if (totalSize == resourcesToReceive) {
            this.resources = resources;
        }
    }
}
