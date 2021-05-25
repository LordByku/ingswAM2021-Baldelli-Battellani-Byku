package it.polimi.ingsw.controller;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.PlayerType;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class CommandBuffer {
    private transient final Person person;
    private final CommandType commandType;
    private boolean completed;

    protected CommandBuffer(CommandType commandType, Person person) throws InvalidCommandException {
        this.commandType = commandType;
        this.person = person;
        completed = false;
    }

    protected static boolean initDiscardsMissing() {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        for (Player player : players) {
            if (player.getPlayerType() == PlayerType.PERSON) {
                Person person = (Person) player;
                if (person.isConnected() && !person.initDiscarded()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected static boolean initSelectsMissing() {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        for (Player player : players) {
            if (player.getPlayerType() == PlayerType.PERSON) {
                Person person = (Person) player;
                if (person.isConnected() && !person.initSelected()) {
                    return true;
                }
            }
        }

        return false;
    }

    public Person getPerson() {
        return person;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean isCompleted() {
        return completed;
    }

    protected void setCompleted() {
        completed = true;
    }

    public abstract boolean isReady();

    public abstract void complete() throws CommandNotCompleteException;

    public abstract boolean cancel();

    public abstract void kill();

    public abstract Consumer<GameStateSerializer> handleMessage(String command, JsonElement value) throws RuntimeException;

    protected ConcreteResourceSet checkResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();
        StrongBox strongBox = person.getBoard().getStrongBox();

        ConcreteResourceSet totalToSpend = new ConcreteResourceSet();

        if (warehouseToSpend.length != warehouse.numberOfDepots()) {
            return null;
        }
        for (int i = 0; i < warehouseToSpend.length; i++) {
            ConcreteResourceSet depotToSpend = warehouseToSpend[i];
            if (!warehouse.getDepotResources(i).contains(depotToSpend)) {
                return null;
            }
            totalToSpend.union(depotToSpend);
        }

        if (!strongBox.containsResources(strongboxToSpend)) {
            return null;
        }
        totalToSpend.union(strongboxToSpend);

        return totalToSpend;
    }

    protected void updateResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend,
                                          Person person, ConcreteResourceSet[] currentWarehouseToSpend, ConcreteResourceSet currentStrongboxToSpend) {
        Warehouse warehouse = person.getBoard().getWarehouse();
        StrongBox strongBox = person.getBoard().getStrongBox();
        for (int i = 0; i < warehouseToSpend.length; ++i) {
            warehouse.removeResources(i, warehouseToSpend[i]);
            currentWarehouseToSpend[i].union(warehouseToSpend[i]);
        }
        strongBox.removeResources(strongboxToSpend);
        currentStrongboxToSpend.union(strongboxToSpend);
    }

    protected ConcreteResourceSet getCurrentTotalToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        ConcreteResourceSet totalToSpend = new ConcreteResourceSet();
        if (warehouseToSpend != null) {
            for (ConcreteResourceSet depotToSpend : warehouseToSpend) {
                totalToSpend.union(depotToSpend);
            }
        }
        if (strongboxToSpend != null) {
            totalToSpend.union(strongboxToSpend);
        }
        return totalToSpend;
    }
}
