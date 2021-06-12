package it.polimi.ingsw.controller;

import org.junit.Test;

public class CommandTypeTest {
    @Test
    public void toStringTest() {
        for(CommandType commandType: CommandType.values()) {
            System.out.println(commandType.toString());
        }
    }
}
