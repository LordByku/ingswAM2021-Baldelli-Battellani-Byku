package it.polimi.ingsw.controller;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PlayLeader extends CommandBuffer {
    private int index;

    protected PlayLeader(CommandType commandType, Person person) throws InvalidCommandException {
        super(commandType, person);

        if (!person.isActivePlayer() || initDiscardsMissing() || initSelectsMissing()) {
            throw new InvalidCommandException();
        }

        index = -1;
    }

    @Override
    public boolean isReady() {
        return index != -1;
    }

    @Override
    public void complete() throws CommandNotCompleteException {
        Person person = getPerson();
        ArrayList<LeaderCard> leaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();
        leaderCards.get(index).play();

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
        if (command.equals("index")) {
            int index = value.getAsInt();
            setIndex(index);
        }

        if (isReady()) {
            complete();
            Person person = getPerson();
            return (serializer) -> {
                serializer.addHandLeaderCards(person);
                serializer.addPlayedLeaderCards(person);
            };
        } else {
            return null;
        }
    }

    private void setIndex(int index) {
        Person person = getPerson();
        ArrayList<LeaderCard> leaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();

        if (index < 0 || index >= leaderCards.size()) {
            return;
        }

        if (leaderCards.get(index).isPlayable()) {
            this.index = index;
        }
    }
}
