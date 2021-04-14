package it.polimi.ingsw.game;

import it.polimi.ingsw.gameZone.GameZone;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private static Game instance;
    private int numberOfPlayers;
    private ArrayList<Player> players;
    private boolean gameStarted;
    private boolean gameEnded;
    private boolean isLastTurn;
    private GameZone gameZone;
    private int currentPlayer;

    private Game() {
        numberOfPlayers = 0;
        players = new ArrayList<>();
        gameStarted = false;
        gameEnded = false;
        isLastTurn = false;
        gameZone = new GameZone();
    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void addPlayer(String nickname) throws FullLobbyException, ExistingNicknameException {
        if(players.size() == 4) {
            throw new FullLobbyException();
        }
        for(Player player: players) {
            if(((Person) player).getNickname().equals(nickname)) {
                throw new ExistingNicknameException();
            }
        }

        players.add(new Person(nickname));
        numberOfPlayers++;
    }

    public void start() throws NoPlayersException, GameAlreadyStartedException {
        if(gameStarted) {
            throw new GameAlreadyStartedException();
        }

        if(players.size() == 0) {
            throw new NoPlayersException();
        }

        Collections.shuffle(players);

        // TODO: handle leader cards distribution

        if(players.size() == 1) {
            players.add(new Computer());
        } else {
            // TODO: handle initial resources distribution
        }

        gameStarted = true;
        currentPlayer = 0;
        players.get(currentPlayer).startTurn();
    }

    protected void setLastTurn() throws GameNotStartedException, GameEndedException {
        if(!gameStarted) {
            throw new GameNotStartedException();
        }
        if(gameEnded) {
            throw new GameEndedException();
        }
        isLastTurn = true;
    }

    protected void handleTurnOrder() throws GameNotStartedException, GameEndedException {
        if(!gameStarted) {
            throw new GameNotStartedException();
        }
        if(gameEnded) {
            throw new GameEndedException();
        }
        currentPlayer = (currentPlayer + 1) % players.size();
        if(isLastTurn && currentPlayer == 0) {
            endGame();
        } else {
            players.get(currentPlayer).startTurn();
        }
    }

    private void endGame() {
        gameEnded = true;
        // TODO
    }
}
