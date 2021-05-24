package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.parsing.InitGameParser;
import it.polimi.ingsw.utility.Deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class InitDiscard extends CommandBuffer {
    private int[] indices;

    protected InitDiscard(CommandType commandType, Person person) throws InvalidCommandException {
        super(commandType, person);

        if (person.initDiscarded()) {
            throw new InvalidCommandException();
        }

        indices = null;
    }

    @Override
    public boolean isReady() {
        return indices != null;
    }

    @Override
    public void complete() throws CommandNotCompleteException {
        if (!isReady()) {
            throw new CommandNotCompleteException();
        }

        ArrayList<LeaderCard> toDiscard = new ArrayList<>();

        Person person = getPerson();
        ArrayList<LeaderCard> leaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();
        for (int index : indices) {
            toDiscard.add(leaderCards.get(index));
        }

        for (LeaderCard leaderCard : toDiscard) {
            leaderCard.initDiscard();
        }

        person.initDiscardDone();

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
        if (command.equals("indices")) {
            JsonArray jsonArray = value.getAsJsonArray();
            int[] indices = Deserializer.getInstance().getIntArray(jsonArray);
            if (indices != null) {
                setIndices(indices);
            }
        }

        if (isReady()) {
            complete();
            Person person = getPerson();
            return (serializer) -> {
                serializer.addHandLeaderCards(person);
            };
        } else {
            return null;
        }
    }

    public void setIndices(int[] indices) {
        if (indices.length != InitGameParser.getInstance().getLeaderCardsToDiscard()) {
            return;
        }

        HashSet<Integer> set = new HashSet<>();
        Person person = getPerson();
        ArrayList<LeaderCard> handLeaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();
        for (int index : indices) {
            if (index < 0 || index >= handLeaderCards.size()) {
                return;
            }
            if (set.contains(index)) {
                return;
            }
            set.add(index);
        }

        this.indices = indices;
    }
}
