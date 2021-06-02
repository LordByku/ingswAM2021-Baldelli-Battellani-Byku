package it.polimi.ingsw.editor.model.resources;

public enum ObtainableResource {
    COIN("COIN"),
    STONE("STONE"),
    SHIELD("SHIELD"),
    SERVANT("SERVANT"),
    CHOICE("CHOICE"),
    FAITHPOINTS("FAITH POINTS");

    private final String string;

    ObtainableResource(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
