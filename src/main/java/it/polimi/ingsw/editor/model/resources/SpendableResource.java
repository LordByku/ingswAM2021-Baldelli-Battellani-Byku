package it.polimi.ingsw.editor.model.resources;

public enum SpendableResource {
    COIN("COIN"),
    STONE("STONE"),
    SHIELD("SHIELD"),
    SERVANT("SERVANT"),
    CHOICE("CHOICE");

    private final String string;

    SpendableResource(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
