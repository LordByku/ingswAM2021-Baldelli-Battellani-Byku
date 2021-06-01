package it.polimi.ingsw.editor.model.resources;

public enum ConcreteResource {
    COIN("COIN"),
    STONE("STONE"),
    SHIELD("SHIELD"),
    SERVANT("SERVANT");

    private final String string;

    ConcreteResource(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
