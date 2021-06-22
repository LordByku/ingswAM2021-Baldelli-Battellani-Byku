package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.parsing.InitGameParser;
import it.polimi.ingsw.utility.Deserializer;

import java.util.HashSet;
import java.util.function.Consumer;

public class InitResources extends CommandBuffer {
    private ConcreteResourceSet[] resources;

    protected InitResources(CommandType commandType, Person person) {
        super(commandType, person);

        if (person.initSelected() && initDiscardsMissing()) {
            throw new InvalidCommandException();
        }

        resources = new ConcreteResourceSet[BoardParser.getInstance().getDepotSizes().size()];
        for(int i = 0; i < resources.length; ++i) {
            resources[i] = new ConcreteResourceSet();
        }
    }

    @Override
    public boolean isReady() {
        ConcreteResourceSet totalResources = new ConcreteResourceSet();
        for(ConcreteResourceSet depotResources: resources) {
            totalResources.union(depotResources);
        }
        Person person = getPerson();
        int playerIndex = Game.getInstance().getPlayerIndex(person);
        return totalResources.size() == InitGameParser.getInstance().getInitResources(playerIndex);
    }

    @Override
    public Consumer<GameStateSerializer> complete() throws InvalidCommandException {
        if (!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();

        person.initSelectDone();

        setCompleted();

        return (serializer) -> {
            serializer.addWarehouse(person);
        };
    }

    @Override
    public Consumer<GameStateSerializer> cancel() {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();
        for(int i = 0; i < resources.length; ++i) {
            warehouse.removeResources(i, resources[i]);
        }

        return (serializer) -> {
            serializer.addWarehouse(person);
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
            Person person = getPerson();
            return (serializer) -> {
                serializer.addWarehouse(person);
            };
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
            totalSize += resources[i].size() + this.resources[i].size();
        }

        int playerIndex = Game.getInstance().getPlayerIndex(person);
        int resourcesToReceive = InitGameParser.getInstance().getInitResources(playerIndex);

        if (totalSize <= resourcesToReceive) {
            for(int i = 0; i < resources.length; ++i) {
                this.resources[i].union(resources[i]);
                warehouse.addResources(i, resources[i]);
            }
        }
    }
}
