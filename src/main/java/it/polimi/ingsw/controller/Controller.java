package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.playerBoard.Board;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Controller {

   public void addResourcesToDepot(Person person, ConcreteResourceSet resources, int depotIndex){
       person.getBoard().getWarehouse().addResources(depotIndex,resources);
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

   public void discardLeaderCard(Person person, int leaderCardIndex) {
       person.getBoard().getLeaderCardArea().getLeaderCards().get(leaderCardIndex).discard();
   }

   public boolean initDiscard(Person person, int [] discardedLeaderCardsIndexes) {

       LeaderCard firstDiscardedLeaderCard = null;
       LeaderCard secondDiscardedLeaderCard= null;

       if (discardedLeaderCardsIndexes.length!=2)
           return false;
       if(discardedLeaderCardsIndexes[0]<0 || discardedLeaderCardsIndexes[0]>3 || discardedLeaderCardsIndexes[1]<0 || discardedLeaderCardsIndexes[1]>3)
           return false;
       if (discardedLeaderCardsIndexes[0]!=discardedLeaderCardsIndexes[1])
           return false;
       firstDiscardedLeaderCard = person.getBoard().getLeaderCardArea().getLeaderCards().get(discardedLeaderCardsIndexes[0]);
       secondDiscardedLeaderCard= person.getBoard().getLeaderCardArea().getLeaderCards().get(discardedLeaderCardsIndexes[1]);

       firstDiscardedLeaderCard.initDiscard();
       secondDiscardedLeaderCard.initDiscard();

       return true;
   }
}
