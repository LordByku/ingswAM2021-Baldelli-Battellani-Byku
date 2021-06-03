package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import it.polimi.ingsw.model.devCards.CardLevel;

import java.util.HashSet;

public class CardSet {
    private final HashSet<CardLevel> levels;
    private int quantity;

    public CardSet() {
        levels = new HashSet<>();
        quantity = 0;
    }

    public void toggle(CardLevel level) {
        if(levels.contains(level)) {
            levels.remove(level);
        } else {
            levels.add(level);
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isActive(CardLevel level) {
        return levels.contains(level);
    }
}
