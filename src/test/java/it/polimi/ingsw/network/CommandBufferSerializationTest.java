package it.polimi.ingsw.network;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.InvalidNicknameException;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.gameZone.LeaderCardsDeck;
import it.polimi.ingsw.utility.JsonUtil;
import org.junit.Test;

import static org.junit.Assert.fail;

public class CommandBufferSerializationTest {
    @Test
    public void serializeInitDiscardTest() {
        CommandType commandType = CommandType.INITDISCARD;
        JsonUtil jsonUtil = JsonUtil.getInstance();
        try {
            Person person = new Person("test");

            Game.reset();
            LeaderCardsDeck leaderCardsDeck = Game.getInstance().getGameZone().getLeaderCardsDeck();
            leaderCardsDeck.assignCards(person.getBoard());

            CommandBuffer commandBuffer = commandType.getCommandBuffer(person);

            JsonObject jsonObject = jsonUtil.serialize(commandBuffer).getAsJsonObject();
            System.out.println(jsonObject.toString());

            String json = "[0, 1]";
            commandBuffer.handleMessage("indices", jsonUtil.parseLine(json));

            jsonObject = jsonUtil.serialize(commandBuffer).getAsJsonObject();
            System.out.println(jsonObject.toString());
        } catch (InvalidNicknameException e) {
            fail();
        }
    }
}
