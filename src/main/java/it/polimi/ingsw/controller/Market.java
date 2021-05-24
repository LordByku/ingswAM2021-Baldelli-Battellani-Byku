package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.gameZone.MarbleMarket;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.utility.Deserializer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Market extends CommandBuffer {
    private boolean rowColSel;
    private int index;
    private boolean selectionLocked;
    private ChoiceResourceSet obtainedResources;
    private int obtainedFaithPoints;
    private ConcreteResourceSet toDiscard;

    protected Market(CommandType commandType, Person person) throws InvalidCommandException {
        super(commandType, person);

        if(!person.isActivePlayer() || person.mainAction() || initDiscardsMissing() || initSelectsMissing()) {
            throw new InvalidCommandException();
        }

        rowColSel = false;
        index = -1;
        selectionLocked = false;
        obtainedResources = null;
        obtainedFaithPoints = -1;
        toDiscard = null;
    }

    @Override
    public boolean isReady() {
        return toDiscard != null;
    }

    @Override
    public void complete() throws CommandNotCompleteException {
        if(!isReady()) {
            throw new CommandNotCompleteException();
        }

        MarbleMarket marbleMarket = Game.getInstance().getGameZone().getMarbleMarket();
        if(rowColSel) {
            marbleMarket.pushRow(index);
        } else {
            marbleMarket.pushColumn(index);
        }

        Person person = getPerson();

        person.getBoard().getFaithTrack().addFaithPoints(obtainedFaithPoints);

        if(toDiscard.size() > 0) {
            ArrayList<Player> players = Game.getInstance().getPlayers();
            for(Player player: players) {
                if(!player.equals(person)) {
                    if(player.getPlayerType() == PlayerType.PERSON) {
                        ((Person) player).getBoard().getFaithTrack().addFaithPoints(toDiscard.size());
                    } else {
                        ((Computer) player).getFaithTrack().addFaithPoints(toDiscard.size());
                    }
                }
            }
        }

        VRSObserver.getInstance().updateVRS();

        person.mainActionDone();
        setCompleted();
    }

    @Override
    public boolean cancel() {
        return !selectionLocked;
    }

    @Override
    public void kill() {
        if(isReady()) {
            complete();
        } else {
            cancel();
        }
    }

    @Override
    public Consumer<GameStateSerializer> handleMessage(String command, JsonElement value) throws RuntimeException {
        switch (command) {
            case "selection": {
                if(selectionLocked) {
                    return null;
                }

                JsonObject jsonObject = value.getAsJsonObject();
                boolean rowColSel = jsonObject.get("rowColSel").getAsBoolean();
                int index = jsonObject.get("index").getAsInt();
                setSelection(rowColSel, index);

                if(obtainedResources != null && obtainedResources.isConcrete()) {
                    toDiscard = obtainedResources.toConcrete();
                }

                return null;
            }
            case "conversion": {
                if(selectionLocked || obtainedResources == null) {
                    return null;
                }

                JsonArray resourcesElement = value.getAsJsonArray();
                ConcreteResource[] resources = Deserializer.getInstance().getConcreteResourceArray(resourcesElement);
                if(resources != null) {
                    convert(resources);
                }

                if(obtainedResources.isConcrete()) {
                    toDiscard = obtainedResources.toConcrete();
                }

                return null;
            }
            case "addToDepot": {
                if(toDiscard == null) {
                    return null;
                }

                selectionLocked = true;

                JsonObject jsonObject = value.getAsJsonObject();
                int depotIndex = jsonObject.get("depotIndex").getAsInt();
                JsonElement resourcesElement = jsonObject.get("resources");
                ConcreteResourceSet resources = Deserializer.getInstance().getConcreteResourceSet(resourcesElement);

                if(resources != null) {
                    if(addToDepot(depotIndex, resources)) {
                        Person person = getPerson();
                        return (serializer) -> {
                            serializer.addWarehouse(person);
                        };
                    }
                }

                return null;
            }
            case "removeFromDepot": {
                if(toDiscard == null) {
                    return null;
                }

                selectionLocked = true;

                JsonObject jsonObject = value.getAsJsonObject();
                int depotIndex = jsonObject.get("depotIndex").getAsInt();
                JsonElement resourcesElement = jsonObject.get("resources");
                ConcreteResourceSet resources = Deserializer.getInstance().getConcreteResourceSet(resourcesElement);

                if(resources != null) {
                    if(removeFromDepot(depotIndex, resources)) {
                        Person person = getPerson();
                        return (serializer) -> {
                            serializer.addWarehouse(person);
                        };
                    }
                }

                return null;
            }
            case "swapFromDepots": {
                if(toDiscard == null) {
                    return null;
                }

                selectionLocked = true;

                JsonObject jsonObject = value.getAsJsonObject();
                int depotIndexA = jsonObject.get("depotIndexA").getAsInt();
                int depotIndexB = jsonObject.get("depotIndexB").getAsInt();

                if(swapFromDepots(depotIndexA, depotIndexB)) {
                    Person person = getPerson();
                    return (serializer) -> {
                        serializer.addWarehouse(person);
                    };
                }

                return null;
            }
            case "confirmWarehouse": {
                if(isReady()) {
                    complete();
                    ArrayList<Player> players = Game.getInstance().getPlayers();
                    return (serializer) -> {
                        serializer.addMarbleMarket();
                        for(Player player: players) {
                            if(player.getPlayerType() == PlayerType.PERSON) {
                                serializer.addFaithTrack((Person) player);
                            }
                        }
                    };
                } else {
                    return null;
                }
            }
            default: {
                return null;
            }
        }
    }

    private boolean checkSelection(boolean rowColSel, int index) {
        MarbleMarket marbleMarket = Game.getInstance().getGameZone().getMarbleMarket();
        if(rowColSel) {
            return index >= 0 && index < marbleMarket.getRows();
        } else {
            return index >= 0 && index < marbleMarket.getColumns();
        }
    }

    private void setSelection(boolean rowColSel, int index) {
        if(checkSelection(rowColSel, index)) {
            MarbleMarket marbleMarket = Game.getInstance().getGameZone().getMarbleMarket();

            Person person = getPerson();
            ChoiceSet conversionEffects = person.getBoard().getConversionEffectArea().getConversionEffects();
            ObtainableResourceSet obtained;
            if(rowColSel) {
                obtained = marbleMarket.selectRow(index, conversionEffects);
            } else {
                obtained = marbleMarket.selectColumn(index, conversionEffects);
            }

            this.rowColSel = rowColSel;
            this.index = index;
            this.obtainedResources = obtained.getResourceSet();
            this.obtainedFaithPoints = obtained.getFaithPoints();
        }
    }

    private void convert(ConcreteResource[] resources) {
        ArrayList<Resource> choiceResources = obtainedResources.getChoiceResources();
        if(resources.length != choiceResources.size()) {
            return;
        }
        for(int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(i);
            if(!choiceResource.canChoose(resources[i])) {
                return;
            }
        }
        for(int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(i);
            choiceResource.makeChoice(resources[i]);
        }
    }

    private boolean addToDepot(int depotIndex, ConcreteResourceSet resources) {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();

        if(!toDiscard.contains(resources)) {
            return false;
        }

        if(depotIndex >= 0 && depotIndex < warehouse.numberOfDepots()) {
            if(warehouse.canAdd(depotIndex, resources)) {
                warehouse.addResources(depotIndex, resources);
                toDiscard.difference(resources);
                return true;
            }
        }

        return false;
    }

    private boolean removeFromDepot(int depotIndex, ConcreteResourceSet resources) {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();

        if(depotIndex >= 0 && depotIndex < warehouse.numberOfDepots()) {
            if (warehouse.getDepotResources(depotIndex).contains(resources)) {
                warehouse.removeResources(depotIndex, resources);
                toDiscard.union(resources);
                return true;
            }
        }

        return false;
    }

    private boolean swapFromDepots(int depotIndexA, int depotIndexB) {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();

        if(depotIndexA >= 0 && depotIndexA < warehouse.numberOfDepots() &&
           depotIndexB >= 0 && depotIndexB < warehouse.numberOfDepots()) {
            if(warehouse.canSwap(depotIndexA, depotIndexB)) {
                warehouse.swapResources(depotIndexA, depotIndexB);
                return true;
            }
        }

        return false;
    }

    public int getIndex() {
        return index;
    }

    public ChoiceResourceSet getObtainedResources() {
        return obtainedResources;
    }

    public ConcreteResourceSet getToDiscard() {
        return toDiscard;
    }
}
