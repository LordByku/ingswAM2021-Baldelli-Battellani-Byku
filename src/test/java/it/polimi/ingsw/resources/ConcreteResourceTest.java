package it.polimi.ingsw.resources;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ConcreteResourceTest {
    @Test
    public void getResourceTest() {
        ConcreteResource coin = ConcreteResource.COIN;
        ConcreteResource shield = ConcreteResource.SHIELD;
        ConcreteResource servant = ConcreteResource.SERVANT;
        ConcreteResource stone = ConcreteResource.STONE;

        assertSame(coin.getResource(), ConcreteResource.COIN);
        assertSame(shield.getResource(), ConcreteResource.SHIELD);
        assertSame(servant.getResource(), ConcreteResource.SERVANT);
        assertSame(stone.getResource(), ConcreteResource.STONE);
    }
}
