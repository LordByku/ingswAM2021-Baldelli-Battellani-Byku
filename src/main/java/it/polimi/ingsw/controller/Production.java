package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.playerBoard.ProductionArea;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
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

        if(!person.isActivePlayer() || person.mainAction() || initDiscardsMissing() || initSelectsMissing()) {
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
        return obtainedResources.isConcrete();
    }

    @Override
    public void complete() throws CommandNotCompleteException {
        if(!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();
        StrongBox strongBox = person.getBoard().getStrongBox();
        FaithTrack faithTrack = person.getBoard().getFaithTrack();

        for(int i = 0; i < warehouseToSpend.length; ++i) {
            warehouse.removeResources(i, warehouseToSpend[i]);
        }
        strongBox.removeResources(strongboxToSpend);

        strongBox.addResources(obtainedResources.toConcrete());
        faithTrack.addFaithPoints(obtainedFaithPoints);

        VRSObserver.getInstance().updateVRS();

        person.mainActionDone();
        setCompleted();
    }

    @Override
    public boolean cancel() {
        return true;
    }

    @Override
    public void kill() {

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

                if(warehouse != null && strongbox != null) {
                    setResourcesToSpend(warehouse, strongbox);
                }

                return checkCompletion();
            }
            case "choiceSelection": {
                if(obtainedResources == null) {
                    return null;
                }

                JsonArray resourcesElement = value.getAsJsonArray();
                ConcreteResource[] resources = Deserializer.getInstance().getConcreteResourceArray(resourcesElement);
                if(resources != null) {
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
        if(isReady()) {
            complete();
            Person person = getPerson();
            return (serializer) -> {
                serializer.addWarehouse(person);
                serializer.addStrongbox(person);
                serializer.addFaithTrack(person);
            };
        } else {
            return null;
        }
    }

    private void setSelection(int[] productionsToActivate) {
        Person person = getPerson();
        ProductionArea productionArea = person.getBoard().getProductionArea();
        HashSet<Integer> set = new HashSet<>();

        for(int productionIndex: productionsToActivate) {
            if(productionIndex < 0 || productionIndex >= productionArea.size() ||
                    productionArea.getProduction(productionIndex) == null) {
                return;
            }
            if(set.contains(productionIndex)) {
                return;
            }
            set.add(productionIndex);
        }

        this.productionsToActivate = productionsToActivate;
        warehouseToSpend = null;
        strongboxToSpend = null;
        obtainedResources = null;
        obtainedFaithPoints = -1;
    }

    private void setResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        ConcreteResourceSet totalToSpend = checkResourcesToSpend(warehouseToSpend, strongboxToSpend);
        if(totalToSpend == null) {
            return;
        }

        Person person = getPerson();
        ProductionArea productionArea = person.getBoard().getProductionArea();

        SpendableResourceSet toSpend = new SpendableResourceSet();
        ObtainableResourceSet obtained = new ObtainableResourceSet();

        for(int productionIndex: productionsToActivate) {
            ProductionDetails productionDetails = productionArea.getProduction(productionIndex);
            toSpend.union(productionDetails.getInput());
            obtained.union(productionDetails.getOutput());
        }

        if(toSpend.match(totalToSpend)) {
            this.warehouseToSpend = warehouseToSpend;
            this.strongboxToSpend = strongboxToSpend;
            this.obtainedResources = obtained.getResourceSet();
            this.obtainedFaithPoints = obtained.getFaithPoints();
        }
    }

    private void selectChoices(ConcreteResource[] resources) {
        ArrayList<Resource> choiceResources = obtainedResources.getChoiceResources();
        if(resources.length != choiceResources.size()) {
            return;
        }
        for(int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(i);
            choiceResource.makeChoice(resources[i]);
        }
    }

    public int[] getProductionsToActivate() {
        return productionsToActivate;
    }

    public ChoiceResourceSet getObtainedResources() {
        return obtainedResources;
    }
}
