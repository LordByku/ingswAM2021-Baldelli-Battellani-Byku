package it.polimi.ingsw.resources.resourceSets;

/**
 * NotSingleTypeException is thrown when a ConcreteResourceSet containing resources
 * of at most one type was expected
 */
public class NotSingleTypeException extends RuntimeException {
    public NotSingleTypeException() {}
}
