package it.polimi.ingsw.resources;

public interface Resource {
    ConcreteResource getResource();

    boolean isConcrete();
}
