package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Person;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

public enum CommandType {
    INITDISCARD(InitDiscard::new, InitDiscard.class),
    INITRESOURCES(InitResources::new, InitResources.class),
    PLAYLEADER(PlayLeader::new, PlayLeader.class),
    DISCARDLEADER(DiscardLeader::new, DiscardLeader.class),
    MARKET(Market::new, Market.class),
    PURCHASE(Purchase::new, Purchase.class),
    PRODUCTION(Production::new, Production.class);

    private final BiFunction<CommandType, Person, CommandBuffer> commandBufferSupplier;
    private final Type commandBufferClass;

    CommandType(BiFunction<CommandType, Person, CommandBuffer> commandBufferSupplier, Type commandBufferClass) {
        this.commandBufferSupplier = commandBufferSupplier;
        this.commandBufferClass = commandBufferClass;
    }

    public CommandBuffer getCommandBuffer(Person person) throws InvalidCommandException {
        return commandBufferSupplier.apply(this, person);
    }

    public Type getCommandBufferClass() {
        return commandBufferClass;
    }
}
