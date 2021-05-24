package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.localModel.Player;

public class InitDiscard extends CommandWindow {
    public InitDiscard(Client client) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);
        int[] indices = new int[words.length];
        try {
            for (int i = 0; i < words.length; ++i) {
                indices[i] = Integer.parseInt(words[i]);
            }
            JsonObject message = buildCommandMessage("indices", JsonUtil.getInstance().serialize(indices));
            client.write(message.toString());
            return;
        } catch (NumberFormatException e) {
        }

        CLI.getInstance().renderWindow(client);
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        CLI.getInstance().showLeaderCards(self.getBoard().getHandLeaderCards());
        CLI.getInstance().initDiscard();
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        return client.getModel().allInitDiscard();
    }

}
