package it.polimi.ingsw.leaderCards;


import it.polimi.ingsw.resources.ConcreteResource;

/**
 * ConversionEffect represents leaderCards' conversion power, which allows to convert a white marble into a ConcreteResource.
 */
public class ConversionEffect {

    /**
     * The resource to convert the white marble into.
     */
    private ConcreteResource resource;

    /**
     * Constructor sets the parameter resource.
     * @param resource the resource to convert the white marble into.
     */
    ConversionEffect(ConcreteResource resource){
        this.resource = resource;
    }

    /**
     * @return the resource to convert the white marble into.
     */
    public ConcreteResource getResource(){ return resource; }
}
