package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.utility.Deserializer;

import java.util.function.Consumer;

public class Purchase extends CommandBuffer {
    private int marketRow, marketCol;
    private int deckIndex;
    private ConcreteResourceSet[] warehouseToSpend;
    private ConcreteResourceSet strongboxToSpend;

    protected Purchase(CommandType commandType, Person person) {
        super(commandType, person);

        if(!person.isActivePlayer() || person.mainAction() || initDiscardsMissing() || initSelectsMissing()) {
            throw new InvalidCommandException();
        }

        marketRow = -1;
        marketCol = -1;
        deckIndex = -1;
        warehouseToSpend = null;
        strongboxToSpend = null;
    }

    @Override
    public boolean isReady() {
        return warehouseToSpend != null && strongboxToSpend != null;
    }

    @Override
    public void complete() throws CommandNotCompleteException {
        if(!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();
        Warehouse warehouse = person.getBoard().getWarehouse();
        StrongBox strongBox = person.getBoard().getStrongBox();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.removeTop(marketRow, marketCol);

        devCard.play(person.getBoard(), deckIndex);

        for(int i = 0; i < warehouseToSpend.length; ++i) {
            warehouse.removeResources(i, warehouseToSpend[i]);
        }
        strongBox.removeResources(strongboxToSpend);

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
                JsonObject jsonObject = value.getAsJsonObject();
                int marketRow = jsonObject.get("row").getAsInt();
                int marketCol = jsonObject.get("column").getAsInt();
                int deckIndex = jsonObject.get("deckIndex").getAsInt();

                setSelection(marketRow, marketCol, deckIndex);

                return null;
            }
            case "spendResources": {
                if(marketRow == -1 || marketCol == -1 || deckIndex == -1) {
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

                if(isReady()) {
                    complete();
                    Person person = getPerson();
                    return (serializer) -> {
                        serializer.addCardMarket();
                        serializer.addDevCards(person);
                        serializer.addWarehouse(person);
                        serializer.addStrongbox(person);
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

    private void setSelection(int marketRow, int marketCol, int deckIndex) {
        Person person = getPerson();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);

        if(devCard.canPlay(person.getBoard(), deckIndex)) {
            this.marketRow = marketRow;
            this.marketCol = marketCol;
            this.deckIndex = deckIndex;
        }
    }

    private void setResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        ConcreteResourceSet totalToSpend = checkResourcesToSpend(warehouseToSpend, strongboxToSpend);
        if(totalToSpend == null) {
            return;
        }

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);
        ConcreteResourceSet requirements = devCard.getReqResources();

        if(requirements.contains(totalToSpend) && totalToSpend.contains(requirements)) {
            this.warehouseToSpend = warehouseToSpend;
            this.strongboxToSpend = strongboxToSpend;
        }
    }

    public int getMarketRow() {
        return marketRow;
    }

    public int getMarketCol() {
        return marketCol;
    }

    public int getDeckIndex() {
        return deckIndex;
    }
}
