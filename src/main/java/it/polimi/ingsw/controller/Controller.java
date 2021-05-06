package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
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
import jdk.internal.vm.compiler.collections.EconomicMap;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Controller {

   public void addResourcesToDepot(int playerIndex, ConcreteResourceSet resources, int depotIndex){
       ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getWarehouse().addResources(depotIndex,resources);
   }
   public void addResourcesToStrongbox(int playerIndex, ConcreteResourceSet resources){
       ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getStrongBox().addResources(resources);
   }
   public void removeResourcesFromDepot(int playerIndex, ConcreteResourceSet resources, int depotIndex){
       ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getWarehouse().removeResources(depotIndex,resources);
   }
   public void removeResourcesFromStrongbox(int playerIndex, ConcreteResourceSet resources){
        ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getStrongBox().removeResources(resources);
   }
   public void swap(int playerIndex, int depotIndexA, int depotIndexB){
       ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getWarehouse().swapResources(depotIndexA,depotIndexB);
   }
   public void playLeaderCard(int playerIndex, int leaderCardIndex){
        ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard().getLeaderCardArea().getLeaderCards().get(leaderCardIndex).play();
   }

   public DevCard purchase(int playerIndex, int row, int column, int deckIndex){
       Board playerBoard = ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard();
       boolean canBuy = Game.getInstance().getGameZone().getCardMarket().top(row, column).canPlay(playerBoard,deckIndex);
       if(!canBuy)
           return null;

       return Game.getInstance().getGameZone().getCardMarket().removeTop(row,column);
   }
   public boolean spendResources(int playerIndex, DevCard card, int deckIndex, ConcreteResourceSet[] warehouseSet, ConcreteResourceSet strongboxSet){
       Board playerBoard = ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard();

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
   public ObtainableResourceSet market(int playerIndex, boolean rowColSelection, int index){
       Board playerBoard = ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard();
       ChoiceSet choiceSet = playerBoard.getConversionEffectArea().getConversionEffects();
       if(rowColSelection)
           return Game.getInstance().getGameZone().getMarbleMarket().selectRow(index,choiceSet);
       else
           return Game.getInstance().getGameZone().getMarbleMarket().selectColumn(index,choiceSet);
   }

   public ConcreteResourceSet choiceResource(int playerIndex, ConcreteResource[] resources, ChoiceResourceSet set){
       boolean isConcrete = set.isConcrete();
       if(isConcrete) return set.toConcrete();
       ArrayList<Resource> choiceResources = set.getChoiceResources();
       if(resources.length != choiceResources.size()) return null;
       for(int i=0; i< resources.length; i++){
           ((ChoiceResource) choiceResources.get(i)).makeChoice(resources[i]);
       }

       return set.toConcrete();
   }

   public ConcreteResourceSet productionIn(int playerIndex,int[] activeSet, ConcreteResource[] resources){
       SpendableResourceSet set = new SpendableResourceSet();
       Board playerBoard = ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard();
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
   public ConcreteResourceSet productionOut(int playerIndex,int[] activeSet, ConcreteResource[] resources){
       ObtainableResourceSet set = new ObtainableResourceSet();
       Board playerBoard = ((Person) Game.getInstance().getPlayers().get(playerIndex)).getBoard();
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
}
