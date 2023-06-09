package it.polimi.ingsw.model.leaderCards;


import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;

/**
 * ConversionEffect represents leaderCards' conversion power, which allows to convert a white marble into a ConcreteResource.
 */
public class ConversionEffect {
    /**
     * The resource to convert the white marble into.
     */
    private final ConcreteResource resource;

    /**
     * Constructor sets the parameter resource.
     *
     * @param resource the resource to convert the white marble into.
     * @throws InvalidResourceException resource is null
     */
    public ConversionEffect(ConcreteResource resource) throws InvalidResourceException {
        if (resource == null) {
            throw new InvalidResourceException();
        }
        this.resource = resource;
    }

    /**
     * @return the resource to convert the white marble into.
     */
    public ConcreteResource getResource() {
        return resource;
    }

    public String getFilename() {
        switch (resource) {
            case COIN:
                return "11";
            case STONE:
                return "10";
            case SHIELD:
                return "9";
            case SERVANT:
                return "8";
            default:
                return null;
        }
    }
}
