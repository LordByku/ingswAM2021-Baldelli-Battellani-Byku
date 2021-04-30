package it.polimi.ingsw.view.cli;

public class CLI {
    public CLI() {}

    public void welcome() {
        System.out.println("---- Masters of Renaissance ----");
        System.out.println("Insert your nickname to continue:");
    }

    public void selectMode() {
        System.out.println("[1] MultiPlayer Game");
        System.out.println("[2] SinglePlayer Game");
        System.out.println("Insert your choice:");
    }
}
