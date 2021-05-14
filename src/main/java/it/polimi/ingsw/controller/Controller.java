package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.gameZone.MarbleMarket;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidDepotIndexException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidResourceLocationOperationException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.*;
import it.polimi.ingsw.model.resources.resourceSets.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Controller {
    private static Controller instance;

    private ArrayList<Person> onceActionPlayer = new ArrayList<>();
    private HashMap<Person,Integer> initialResources;

    private ResourceSet resourcesToObtain;
    private int faithPointToObtain;

    private DevCard cardToBuy;

    private Controller() {}

    public synchronized static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public boolean addResourcesToDepot(Person person, ConcreteResourceSet resources, int depotIndex) {
        ConcreteResourceSet concreteToObtain = concreteToObtain();
        if(!concreteToObtain.contains(resources)) {
            return false;
        }
        try {
            person.getBoard().getWarehouse().addResources(depotIndex, resources);
            concreteToObtain.difference(resources);
            resourcesToObtain = concreteToObtain;
            return true;
        } catch (InvalidResourceException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            return false;
        }
    }

    public boolean removeResourcesFromDepot(Person person, ConcreteResourceSet resources, int depotIndex){
        ConcreteResourceSet concreteToObtain = concreteToObtain();
        try {
            person.getBoard().getWarehouse().removeResources(depotIndex, resources);
            concreteToObtain.union(resources);
            resourcesToObtain = concreteToObtain;
            return true;
        } catch (InvalidResourceException | InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            return false;
        }
    }

    public boolean swapResourcesFromDepots(Person person, int depotIndexA, int depotIndexB) {
        try {
            person.getBoard().getWarehouse().swapResources(depotIndexA, depotIndexB);
            return true;
        } catch (InvalidDepotIndexException | InvalidResourceLocationOperationException e) {
            return false;
        }
    }

    public boolean addResourcesToStrongbox(Person person, ConcreteResourceSet resources){
        try{
            person.getBoard().getStrongBox().addResources(resources);
            return true;
        }catch (InvalidResourceSetException e){
            return false;
        }
    }

    public boolean removeResourcesFromStrongbox(Person person, ConcreteResourceSet resources){
        try{
            person.getBoard().getStrongBox().removeResources(resources);
            return true;
        }
        catch (InvalidResourceSetException | InvalidResourceLocationOperationException e){
            return false;
        }
    }

    public boolean purchase(Person person, int row, int column, int deckIndex){
        Board playerBoard = person.getBoard();
        boolean canBuy = Game.getInstance().getGameZone().getCardMarket().top(row, column).canPlay(playerBoard,deckIndex);
        if(!canBuy)
            return false;

        cardToBuy = Game.getInstance().getGameZone().getCardMarket().removeTop(row,column);
        return true;
    }

    public boolean spendResources(Person person, int deckIndex, ConcreteResourceSet[] warehouseSet, ConcreteResourceSet strongboxSet){
        Board playerBoard = person.getBoard();

        //Checks if we have selected the right resources
        ConcreteResourceSet set = new ConcreteResourceSet();
        for(ConcreteResourceSet crs: warehouseSet) {
            set.union(crs);
        }
        set.union(strongboxSet);

        if(!(set.contains(cardToBuy.getReqResources())) && (cardToBuy.getReqResources().contains(set))){
            System.out.println("set: "+ set.getCLIString());
            System.out.println("reqResources: " + cardToBuy.getReqResources().getCLIString());
            System.out.println("set != reqResources");
            return false;
        }

        try {
            //remove resources from warehouse
            for (int i = 0; i < warehouseSet.length; i++) {
                playerBoard.getWarehouse().removeResources(i, warehouseSet[i]);
            }

            //remove resources from strongbox
            removeResourcesFromStrongbox(person,strongboxSet);
            try{
                cardToBuy.play(playerBoard, deckIndex);
                return true;
            }catch (InvalidBoardException e){
                return false;
            }
        }
        catch ( InvalidDepotIndexException | InvalidResourceSetException | InvalidResourceLocationOperationException e){
            System.out.println("Exception");
            return false;
        }

    }

    public boolean market(Person person, boolean rowColSelection, int index){
        Board playerBoard = person.getBoard();
        ChoiceSet choiceSet = playerBoard.getConversionEffectArea().getConversionEffects();
        MarbleMarket market = Game.getInstance().getGameZone().getMarbleMarket();
        ObtainableResourceSet obtainableResourceSet;
        if(rowColSelection) {
            if(index < 0 || index >= market.getRows()) {
                return false;
            }
            obtainableResourceSet = market.selectRow(index, choiceSet);
            market.pushRow(index);
        } else {
            if(index < 0 || index >= market.getColumns()) {
                return false;
            }
            obtainableResourceSet = market.selectColumn(index, choiceSet);
            market.pushColumn(index);
        }

        resourcesToObtain = obtainableResourceSet.getResourceSet();
        faithPointToObtain = obtainableResourceSet.getFaithPoints();

        return true;
    }

    public ConcreteResourceSet choiceResource(Person person, ConcreteResource[] resources, ChoiceResourceSet set){
        boolean isConcrete = set.isConcrete();
        if(isConcrete) return set.toConcrete();
        ArrayList<Resource> choiceResources = set.getChoiceResources();
        if(resources.length != choiceResources.size()) return null;
        for(int i=0; i< resources.length; i++){
            ((ChoiceResource) choiceResources.get(i)).makeChoice(resources[i]);
        }

        return set.toConcrete();
    }

    public boolean toObtainIsConcrete() {
        return resourcesToObtain.isConcrete();
    }

    public int toObtainChoices() {
        if(toObtainIsConcrete()) {
            return 0;
        } else {
            return ((ChoiceResourceSet) resourcesToObtain).getChoiceResources().size();
        }
    }

    public ConcreteResourceSet concreteToObtain() {
        if(toObtainIsConcrete()) {
            return resourcesToObtain.toConcrete();
        } else {
            return null;
        }
    }

    public boolean whiteMarble(ConcreteResource[] resources) {
        ArrayList<Resource> choiceResources = ((ChoiceResourceSet) resourcesToObtain).getChoiceResources();

        if(choiceResources.size() != resources.length) {
            return false;
        }

        for(int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(i);
            if(!choiceResource.canChoose(resources[i])) {
                return false;
            }
        }

        for(int i = 0; i < resources.length; ++i) {
            ChoiceResource choiceResource = (ChoiceResource) choiceResources.get(i);
            choiceResource.makeChoice(resources[i]);
        }

        return true;
    }

    public void confirmWarehouse(Person person) {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        if(faithPointToObtain > 0) {
            person.getBoard().getFaithTrack().addFaithPoints(faithPointToObtain);
        }
        faithPointToObtain = 0;

        if(resourcesToObtain.size() > 0) {
            for(Player player: players) {
                if(!player.equals(person)) {
                    ((Person) player).getBoard().getFaithTrack().addFaithPoints(resourcesToObtain.size());
                }
            }
        }
        resourcesToObtain = null;

        VRSObserver.getInstance().updateVRS();
    }

    public ConcreteResourceSet productionIn(Person person, int[] activeSet, ConcreteResource[] resources){
        SpendableResourceSet set = new SpendableResourceSet();
        Board playerBoard = person.getBoard();
        for(int index: activeSet){
            set.union(playerBoard.getProductionArea().getProductionDetails().get(index).getInput());
        }
        if(resources.length != set.getResourceSet().getChoiceResources().size()) return null;

        for(int i=0; i< resources.length; i++){
            ((ChoiceResource) set.getResourceSet().getChoiceResources().get(i)).makeChoice(resources[i]);
        }
        ConcreteResourceSet concreteResourceSet = set.getResourceSet().toConcrete();
        if(!playerBoard.containsResources(concreteResourceSet)) return null;
        return concreteResourceSet;
    }

    public ConcreteResourceSet productionOut(Person person,int[] activeSet, ConcreteResource[] resources){
        ObtainableResourceSet set = new ObtainableResourceSet();
        Board playerBoard = person.getBoard();
        for(int index: activeSet){
            set.union(playerBoard.getProductionArea().getProductionDetails().get(index).getOutput());
        }
        playerBoard.getFaithTrack().addFaithPoints(set.getFaithPoints());
        if(resources.length != set.getResourceSet().getChoiceResources().size()) return null;
        for(int i=0; i< resources.length; i++){
            ((ChoiceResource) set.getResourceSet().getChoiceResources().get(i)).makeChoice(resources[i]);
        }
        ConcreteResourceSet concreteResourceSet = set.getResourceSet().toConcrete();
        if(!playerBoard.containsResources(concreteResourceSet)) return null;
        return concreteResourceSet;
    }


    public boolean playLeaderCard(Person person, int leaderCardIndex){
        ArrayList<LeaderCard> leaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();

        if(leaderCardIndex < 0 || leaderCardIndex >= leaderCards.size()) {
            return false;
        }

        if(leaderCards.get(leaderCardIndex).isPlayable()) {
            leaderCards.get(leaderCardIndex).play();
            return true;
        } else {
            return false;
        }
    }

    public boolean discardLeaderCard(Person person, int leaderCardIndex) {
        ArrayList<LeaderCard> leaderCards = person.getBoard().getLeaderCardArea().getLeaderCards();

        if(leaderCardIndex < 0 || leaderCardIndex >= leaderCards.size()) {
            return false;
        }

        leaderCards.get(leaderCardIndex).discard();
        return true;
    }

    public boolean initDiscard(Person person, int [] discardedLeaderCardsIndexes) {

        if(person.getBoard().getLeaderCardArea().getLeaderCards().size()<=2)
            //Already discarded
            return false;

        if (discardedLeaderCardsIndexes.length != 2)
            return false;
        if(discardedLeaderCardsIndexes[0] < 0 || discardedLeaderCardsIndexes[0] > 3 || discardedLeaderCardsIndexes[1] < 0 || discardedLeaderCardsIndexes[1] > 3)
            return false;
        if (discardedLeaderCardsIndexes[0] == discardedLeaderCardsIndexes[1])
            return false;
        LeaderCard firstDiscardedLeaderCard = person.getBoard().getLeaderCardArea().getLeaderCards().get(discardedLeaderCardsIndexes[0]);
        LeaderCard secondDiscardedLeaderCard = person.getBoard().getLeaderCardArea().getLeaderCards().get(discardedLeaderCardsIndexes[1]);

        firstDiscardedLeaderCard.initDiscard();
        secondDiscardedLeaderCard.initDiscard();

        onceActionPlayer.add(person);

        return true;
    }

    public boolean initResources(Person person, ConcreteResourceSet[] resources) {
        Warehouse warehouse = person.getBoard().getWarehouse();

        if(!onceActionPlayer.contains(person))
            return false;

        if(resources.length != warehouse.numberOfDepots()) {
            return false;
        }
        HashSet<ConcreteResource> resourceTypes = new HashSet<>();
        int totalSize = 0;
        for(int i = 0; i < resources.length; ++i) {
            if(!resources[i].isSingleType()) {
                return false;
            }
            if(!warehouse.canAdd(i, resources[i])) {
                return false;
            }
            ConcreteResource resourceType = resources[i].getResourceType();
            if(resourceType != null) {
                if(resourceTypes.contains(resourceType)) {
                    return false;
                }
                resourceTypes.add(resources[i].getResourceType());
            }
            totalSize += resources[i].size();
        }

        if(totalSize != getInitResources(person)) {
            return false;
        }

        for(int i = 0; i < resources.length; ++i) {
            warehouse.addResources(i, resources[i]);
        }

        onceActionPlayer.remove(person);

        return true;
    }

    public int getInitResources(Person person) {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        switch (players.indexOf(person)) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    public int getInitFaithPoints(Person person) {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        switch (players.indexOf(person)) {
            case 2:
            case 3:
                return 1;
            default:
                return 0;
        }
    }

    public void handleInitialResources() {
        ArrayList<Player> players = Game.getInstance().getPlayers();
        initialResources = new HashMap<>();

        for (Player player : players) {
            Person person = (Person) player;

            int initFaithPoints = getInitFaithPoints(person);
            if (initFaithPoints > 0) {
                person.getBoard().getFaithTrack().addFaithPoints(initFaithPoints);
            }
        }

        VRSObserver.getInstance().updateVRS();
    }

    public void endTurn(Person person) {
        try {
            person.endTurn();
        } catch (GameEndedException | GameNotStartedException e) {
            e.printStackTrace();
        }
    }
}
