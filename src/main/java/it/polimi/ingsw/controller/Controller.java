package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Depot;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;
import java.util.HashSet;

public class Controller {
    private static Controller instance;

    private ArrayList<Person> onceActionPlayer=null;

    private Controller() {}

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void addResourcesToDepot(Person person, ConcreteResourceSet resources, int depotIndex) {
        person.getBoard().getWarehouse().addResources(depotIndex, resources);
    }

    public void addResourcesToStrongbox(Person person, ConcreteResourceSet resources){
        person.getBoard().getStrongBox().addResources(resources);
    }

    public void removeResourcesFromDepot(Person person, ConcreteResourceSet resources, int depotIndex){
        person.getBoard().getWarehouse().removeResources(depotIndex,resources);
    }

    public void removeResourcesFromStrongbox(Person person, ConcreteResourceSet resources){
        person.getBoard().getStrongBox().removeResources(resources);
    }

    public void swap(Person person, int depotIndexA, int depotIndexB){
        person.getBoard().getWarehouse().swapResources(depotIndexA,depotIndexB);
    }

    public void playLeaderCard(Person person, int leaderCardIndex){
        person.getBoard().getLeaderCardArea().getLeaderCards().get(leaderCardIndex).play();
    }

    public DevCard purchase(Person person, int row, int column, int deckIndex){
        Board playerBoard = person.getBoard();
        boolean canBuy = Game.getInstance().getGameZone().getCardMarket().top(row, column).canPlay(playerBoard,deckIndex);
        if(!canBuy)
            return null;

        return Game.getInstance().getGameZone().getCardMarket().removeTop(row,column);
    }

    public boolean spendResources(Person person, DevCard card, int deckIndex, ConcreteResourceSet[] warehouseSet, ConcreteResourceSet strongboxSet){
        Board playerBoard = person.getBoard();

        //Checks if we have selected the right resources
        ConcreteResourceSet set = new ConcreteResourceSet();
        for(ConcreteResourceSet crs: warehouseSet) {
            set.union(crs);
        }
        set.union(strongboxSet);
        if(!set.equals(card.getReqResources()))
            return false;

        //remove resources from warehouse
        for(int i=0; i<warehouseSet.length; i++){
            playerBoard.getWarehouse().removeResources(i,warehouseSet[i]);
        }

        //remove resources from strongbox
        playerBoard.getStrongBox().removeResources(strongboxSet);

        card.play(playerBoard, deckIndex);
        return true;
    }

    public ObtainableResourceSet market(Person person, boolean rowColSelection, int index){
        Board playerBoard = person.getBoard();
        ChoiceSet choiceSet = playerBoard.getConversionEffectArea().getConversionEffects();
        if(rowColSelection)
            return Game.getInstance().getGameZone().getMarbleMarket().selectRow(index,choiceSet);
        else
            return Game.getInstance().getGameZone().getMarbleMarket().selectColumn(index,choiceSet);
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

    public ConcreteResourceSet productionIn(Person person,int[] activeSet, ConcreteResource[] resources){
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

    public boolean discardLeaderCard(Person person, int leaderCardIndex) {

        if(person.getBoard().getLeaderCardArea().getLeaderCards().size()==2 && (leaderCardIndex<0 || leaderCardIndex >1) || person.getBoard().getLeaderCardArea().getLeaderCards().size()==1 && (leaderCardIndex!=0) )
            return false;
        person.getBoard().getLeaderCardArea().getLeaderCards().get(leaderCardIndex).discard();
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

        if(totalSize != Game.getInstance().getInitialResources(person)) {
            return false;
        }

        for(int i = 0; i < resources.length; ++i) {
            warehouse.addResources(i, resources[i]);
        }

        onceActionPlayer.remove(person);
        
        return true;
    }

    public void addInitialFaithPoints() {
        ArrayList<Player> players = Game.getInstance().getPlayers();

        for(int i = 2; i < players.size(); ++i) {
            ((Person) players.get(i)).getBoard().getFaithTrack().addFaithPoints(1);
        }

        VRSObserver.getInstance().updateVRS();
    }

}
