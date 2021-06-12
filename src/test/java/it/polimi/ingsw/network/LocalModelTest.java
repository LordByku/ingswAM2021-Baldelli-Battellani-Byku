package it.polimi.ingsw.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.view.localModel.LocalModel;
import org.junit.Test;

public class LocalModelTest {
    @Test
    public void deserializationTest() {
        JsonParser parser = new JsonParser();
        String jsonString = "{\"gameZone\":{\"marbleMarket\":{\"market\":[[\"WHITE\",\"GREY\",\"WHITE\",\"BLUE\"],[\"YELLOW\",\"YELLOW\",\"PURPLE\",\"PURPLE\"],[\"WHITE\",\"BLUE\",\"WHITE\",\"RED\"]],\"freeMarble\":\"GREY\"},\"cardMarket\":{\"market\":[[{\"topCard\":\"8\",\"quantity\":\"4\"},{\"topCard\":\"6\",\"quantity\":\"4\"},{\"topCard\":\"11\",\"quantity\":\"4\"},{\"topCard\":\"9\",\"quantity\":\"4\"}],[{\"topCard\":\"24\",\"quantity\":\"4\"},{\"topCard\":\"22\",\"quantity\":\"4\"},{\"topCard\":\"23\",\"quantity\":\"4\"},{\"topCard\":\"21\",\"quantity\":\"4\"}],[{\"topCard\":\"40\",\"quantity\":\"4\"},{\"topCard\":\"46\",\"quantity\":\"4\"},{\"topCard\":\"35\",\"quantity\":\"4\"},{\"topCard\":\"41\",\"quantity\":\"4\"}]]}},\"players\":[{\"nickname\":\"aa\",\"inkwell\":true,\"initDiscard\":false,\"initResources\":true,\"mainAction\":false,\"board\":{\"faithTrack\":{\"position\":0,\"receivedFavors\":[],\"computerPosition\":0},\"warehouse\":{\"depots\":[{\"resources\":{}},{\"resources\":{}},{\"resources\":{}}]},\"strongbox\":{\"content\":{\"resources\":{}}},\"devCards\":{\"decks\":[[],[],[]]},\"playedLeaderCards\":{\"leaderCards\":[]},\"handLeaderCards\":{\"leaderCards\":[0,15,7,9]}}}]}";
        JsonElement json = parser.parse(jsonString);

        LocalModel localModel = Deserializer.getInstance().getLocalModel(json);
    }
}
