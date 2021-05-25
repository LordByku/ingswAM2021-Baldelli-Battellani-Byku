package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.function.Consumer;

public class Purchase extends CommandBuffer {
    private int marketRow, marketCol;
    private int deckIndex;
    private ConcreteResourceSet[] warehouseToSpend;
    private ConcreteResourceSet strongboxToSpend;

    protected Purchase(CommandType commandType, Person person) {
        super(commandType, person);

        if (!person.isActivePlayer() || person.mainAction() || initDiscardsMissing() || initSelectsMissing()) {
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
        if (deckIndex == -1 || warehouseToSpend == null || strongboxToSpend == null) {
            return false;
        }

        Person person = getPerson();
        ConcreteResourceSet totalToSpend = getCurrentTotalToSpend();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);
        ConcreteResourceSet requirements = devCard.getReqResources();
        requirements = person.getBoard().getDiscountArea().applyDiscount(requirements);

        return totalToSpend.contains(requirements);
    }

    @Override
    public Consumer<GameStateSerializer> complete() throws CommandNotCompleteException {
        if (!isReady()) {
            throw new CommandNotCompleteException();
        }

        Person person = getPerson();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.removeTop(marketRow, marketCol);

        devCard.play(person.getBoard(), deckIndex);

        person.mainActionDone();
        setCompleted();

        return (serializer) -> {
            serializer.addCardMarket();
            serializer.addDevCards(person);
            serializer.addWarehouse(person);
            serializer.addStrongbox(person);
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
            case "cardSelection": {
                JsonObject jsonObject = value.getAsJsonObject();
                int marketRow = jsonObject.get("row").getAsInt();
                int marketCol = jsonObject.get("column").getAsInt();

                setCardSelection(marketRow, marketCol);

                return null;
            }
            case "deckSelection": {
                if (marketRow == -1 || marketCol == -1) {
                    return null;
                }

                int deckIndex = value.getAsInt();

                setDeckSelection(deckIndex);

                return null;
            }
            case "spendResources": {
                if (marketRow == -1 || marketCol == -1) {
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
            default: {
                return null;
            }
        }
    }

    private void setCardSelection(int marketRow, int marketCol) {
        if (marketRow < 0 || marketRow >= CardLevel.values().length ||
                marketCol < 0 || marketCol >= CardColour.values().length) {
            return;
        }

        Person person = getPerson();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);
        ConcreteResourceSet requirements = devCard.getReqResources();
        requirements = person.getBoard().getDiscountArea().applyDiscount(requirements);

        if (person.getBoard().containsResources(requirements)) {
            this.marketRow = marketRow;
            this.marketCol = marketCol;
            this.deckIndex = -1;
            warehouseToSpend = null;
            strongboxToSpend = null;
        }
    }

    private void setDeckSelection(int deckIndex) {
        if (deckIndex < 0 || deckIndex >= BoardParser.getInstance().getDevelopmentCardsSlots()) {
            return;
        }

        Person person = getPerson();

        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);

        if (devCard.canPlay(person.getBoard(), deckIndex)) {
            this.deckIndex = deckIndex;
            warehouseToSpend = new ConcreteResourceSet[BoardParser.getInstance().getDepotSizes().size()];
            for (int i = 0; i < warehouseToSpend.length; ++i) {
                warehouseToSpend[i] = new ConcreteResourceSet();
            }
            strongboxToSpend = new ConcreteResourceSet();
        }
    }

    private void setResourcesToSpend(ConcreteResourceSet[] warehouseToSpend, ConcreteResourceSet strongboxToSpend) {
        ConcreteResourceSet totalToSpend = checkResourcesToSpend(warehouseToSpend, strongboxToSpend);
        if (totalToSpend == null) {
            return;
        }

        Person person = getPerson();
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        DevCard devCard = cardMarket.top(marketRow, marketCol);
        ConcreteResourceSet requirements = devCard.getReqResources();
        requirements = person.getBoard().getDiscountArea().applyDiscount(requirements);

        totalToSpend.union(getCurrentTotalToSpend());

        if (requirements.contains(totalToSpend)) {
            updateResourcesToSpend(warehouseToSpend, strongboxToSpend, person, this.warehouseToSpend, this.strongboxToSpend);
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

    public ConcreteResourceSet getCurrentTotalToSpend() {
        return getCurrentTotalToSpend(warehouseToSpend, strongboxToSpend);
    }
}
