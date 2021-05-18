package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.cli.CLI;

public class PurchaseDevCard extends ClientState {
    int row;
    int column;
    int deckIndex;

    public PurchaseDevCard(){
        CLI.getInstance().purchaseDevCard();
    }
    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status){
            case "error":{
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().purchaseDevCard();
                break;
            }
            case "ok":{
                String type = ClientParser.getInstance().getType(json);
                if (type.equals("confirm")) {
                    CLI.getInstance().purchaseDevCardSuccess();
                    Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
                    CLI.getInstance().showWarehouseAndStrongbox(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards(), playerBoard.getStrongBox());
                    int numOfDepots = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().size();
                    int cardId = client.getModel().getGameZone().getCardMarket().getDevCard(row,column);
                    ConcreteResourceSet set = DevCardsParser.getInstance().getCard(cardId).getReqResources();
                    client.setState(new SpendResourcesWarehouse(numOfDepots,set, deckIndex));
                } else {
                    CLI.getInstance().unexpected();
                }
                break;
            }
            default: {
                CLI.getInstance().unexpected();
            }
        }
    }

    protected void handleSelection(Client client, int rowIndex, int columnIndex, int deckIndex){
        JsonObject jsonObject = new JsonObject();
        JsonObject message = new JsonObject();
        message.addProperty("row", rowIndex);
        message.addProperty("column", columnIndex);
        message.addProperty("deckIndex", deckIndex);
        jsonObject.addProperty("command", "purchase");
        jsonObject.add("message", message);
        client.write(jsonObject.toString());
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")){
            client.setState(new PurchaseMenu());
        }
        else{
            try {
                String[] arr = line.split(" ");
                if(arr.length==3) {
                    int rowIndex = Integer.parseInt(arr[0]);
                    int columnIndex = Integer.parseInt(arr[1]);
                    int deckIndex = Integer.parseInt(arr[2]);

                    int rowLength = CardLevel.values().length;
                    int columnLength = CardColour.values().length;
                    int deckLength = BoardParser.getInstance().getDevelopmentCardsSlots();

                    if (rowIndex >= 0 && rowIndex < rowLength && columnIndex >= 0 && columnIndex < columnLength && deckIndex >= 0 && deckIndex < deckLength) {
                        this.row = rowIndex;
                        this.column = columnIndex;
                        this.deckIndex=deckIndex;
                        handleSelection(client,rowIndex,columnIndex,deckIndex);
                    } else {
                        CLI.getInstance().purchaseDevCard();
                    }
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
