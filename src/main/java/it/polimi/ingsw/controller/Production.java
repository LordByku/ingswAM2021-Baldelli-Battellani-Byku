package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.PlayerType;
import it.polimi.ingsw.model.playerBoard.ProductionArea;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.utility.Deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class Production extends CommandBuffer {
    private int[] productionsToActivate;
    private ConcreteResourceSet[] warehouseToSpend;
    private ConcreteResourceSet strongboxToSpend;
    private ChoiceResourceSet obtainedResources;
    private int obtainedFaithPoints;

    protected Production(CommandType commandType, Person person) {
        super(commandType, person);

        if (!person.isActivePlayer() || person.mainAction() || initDiscardsMissing() || initSelectsMissing()) {
            throw new InvalidCommandException();
        }

        productionsToActivate = null;
        warehouseToSpend = null;
        strongboxToSpend = null;
        obtainedResources = null;
        obtainedFaithPoints = -1;
    }

    @Override
    public boolean isReady() {
        return obtainedResources != null && obtainedResources.isConcrete();
    }

    @Override
    public Consumer<GameStateSerializer> complete() throws CommandNotCompleteException {
        if (!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();
        StrongBox strongBox = person.getBoard().getStrongBox();
        FaithTrack faithTrack = person.getBoard().getFaithTrack();

        strongBox.addResources(obtainedResources.toConcrete());
        faithTrack.addFaithPoints(obtainedFaithPoints);

        VRSObserver.getInstance().updateVRS();

        person.mainActionDone();
        setCompleted();

        ArrayList<Player> players = Game.getInstance().getPlayers();
        return (serializer) -> {
            serializer.addWarehouse(person);
            serializer.addStrongbox(person);
            for (Player player : players) {
                if (player.getPlayerType() == PlayerType.PERSON) {
                    serializer.addFaithTrack((Person) player);
                }
            }
        };
    }

    @Override
    public Consumer<GameStateSerializer> cancel() {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();
        StrongBox strongBox = person.getBoard().getStrongBox();

        if (warehouseToSpend != null) {
            for (int i = 0; i < warehouseToSpend.length; ++i) {
                warehouse.addResources(i, warehouseToSpend[i]);
            }
        }
        if (strongboxToSpend != null) {
            strongBox.addResources(strongboxToSpend);
        }

        return (serializer) -> {
            serializer.addWarehouse(person);
            serializer.addStrongbox(person);
        };
    }

    @Override
    public Consumer<GameStateSerializer> handleMessage(String command, JsonElement value) throws RuntimeException {
        switch (command) {
            case "selection": {
                JsonArray productionsElement = value.getAsJsonArray();
                int[] productionsToActivate = Deserializer.getInstance().getIntArray(productionsElement);

                setSelection(productionsToActivate);

                return null;
            }
            case "spendResources": {
                if (productionsToActivate == null) {
                    return null;
                }

                JsonObject jsonObject = value.getAsJsonObject();
                JsonArray warehouseElement = jsonObject.getAsJsonArray("warehouse");
                ConcreteResourceSet[] warehouse = Deserializer.getInstance().getConcreteResourceSetArray(warehouseElement);
                JsonElement strongBoxElement = jsonObject.get("strongbox");
                ConcreteResourceSet strongbox = Deserializer.getInstance().getConcreteResourceSet(strongBoxElement);

                if (warehouse != null && strongbox != null) {
                    setResourcesToSpend(warehouse, strongbox);
                }

                return checkCompletion();
            }
            case "choiceSelection": {
                if (obtainedResources == null) {
                    return null;
                }

                JsonArray resourcesArray = value.getAsJsonArray();
                ConcreteResource[] resources = Deserializer.getInstance().getConcreteResourceArray(resourcesArray);
                if (resources != null) {
                    selectChoices(resources);
                }

                return checkCompletion();
            }
            default: {
                return null;
            }
        }
    }

    private Consumer<GameStateSerializer> checkCompletion() {
        if (isReady()) {
            return complete();
        } else {
            Person person = getPerson();
            return (serializer) -> {
                serializer.addWarehouse(person);
                serializer.addStrongbox(person);
            };
        }
    }

    private void setSelection(int[] productionsToActivate) {
        Person person = getPerson();
        ProductionArea productionArea = person.getBoard().getProductionArea();
        HashSet<Integer> set = new HashSet<>();

        for (int productionIndex : productionsToActivate) {
            if (productionIndex < 0 || productionIndex >= productionArea.size() ||
                    productionArea.getProduction(productionIndex) == null) {
                return;
            }
            if (set.contains(productionIndex)) {
                return;
            }
            set.add(productionIndex);
        }

        SpendableResourceSet toSpend = new SpendableResourceSet();
        for (int productionIndex : productionsToActivate) {
            ProductionDetails productionDetails = productionArea.getProduction(productionIndex);
            toSpend = toSpend.union(productionDetails.getInput());
        }

        if (person.getBoard().getResources().containsResources(toSpend.getResourceSet())) {
            this.productionsToActivate = productionsToActivate;
            warehouseToSpend = new ConcreteResourceSet[person.getBoard().getWarehouse().numberOfDepots()];
            for (int i = 0; i < warehouseToSpend.length; ++i) {
                warehouseToSpend[i] = new ConcreteResourceSet();
            }
            strongboxToSpend = new ConcreteResourceSet();
            obtainedResources = null;
            obtainedFaithPoints = -1;
        }
    }

    private void setResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        ConcreteResourceSet totalToSpend = checkResourcesToSpend(warehouseToSpend, strongboxToSpend);

        if (totalToSpend == null) {
            return;
        }

        Person person = getPerson();
        ProductionArea productionArea = person.getBoard().getProductionArea();

        SpendableResourceSet toSpend = new SpendableResourceSet();
        ObtainableResourceSet obtained = new ObtainableResourceSet();

        for (int productionIndex : productionsToActivate) {
            ProductionDetails productionDetails = productionArea.getProduction(productionIndex);
            toSpend = toSpend.union(productionDetails.getInput());
            obtained = obtained.union(productionDetails.getOutput());
        }

        totalToSpend.union(getCurrentTotalToSpend());

        if (toSpend.match(totalToSpend)) {
            updateResourcesToSpend(warehouseToSpend, strongboxToSpend, person, this.warehouseToSpend, this.strongboxToSpend);

            if (toSpend.exactMatch(totalToSpend)) {
                this.obtainedResources = obtained.getResourceSet();
                this.obtainedFaithPoints = obtained.getFaithPoints();
            }
        }
    }

    private void selectChoices(ConcreteResource[] resources) {
        ArrayList<ChoiceResource> choiceResources = obtainedResources.getChoiceResources();
        if (resources.length != choiceResources.size()) {
            return;
        }
        for (int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = choiceResources.get(i);
            choiceResource.makeChoice(resources[i]);
        }
    }

    public int[] getProductionsToActivate() {
        return productionsToActivate;
    }

    public ChoiceResourceSet getObtainedResources() {
        return obtainedResources;
    }

    public ConcreteResourceSet getCurrentTotalToSpend() {
        return getCurrentTotalToSpend(warehouseToSpend, strongboxToSpend);
    }
}
