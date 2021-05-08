package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.gameZone.GameZone;
import it.polimi.ingsw.model.playerBoard.Board;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private static Game instance;
    private int numberOfPlayers;
    private final ArrayList<Player> players;
    private volatile boolean gameStarted;
    private volatile boolean gameEnded;
    private boolean isLastTurn;
    private final GameZone gameZone;
    private int currentPlayer;

    private Game() {
        numberOfPlayers = 0;
        players = new ArrayList<>();
        gameStarted = false;
        gameEnded = false;
        isLastTurn = false;
        gameZone = new GameZone();
    }

    public synchronized static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Player addPlayer(String nickname)
            throws FullLobbyException, ExistingNicknameException, InvalidNicknameException, GameAlreadyStartedException {
        if(gameStarted) {
            throw new GameAlreadyStartedException();
        }
        synchronized (players) {
            if(players.size() == 4) {
                throw new FullLobbyException();
            }
            for(Player player: players) {
                if(((Person) player).getNickname().equals(nickname)) {
                    throw new ExistingNicknameException();
                }
            }

            Person player = new Person(nickname);
            players.add(player);
            if(++numberOfPlayers == 1) {
                setNewHost();
            }
            return player;
        }
    }

    public void startMultiPlayer() throws NotEnoughPlayersException, GameAlreadyStartedException {
        if(gameStarted) {
            throw new GameAlreadyStartedException();
        }

        if(players.size() < 2) {
            throw new NotEnoughPlayersException();
        }

        Collections.shuffle(players);

        for(Player player: players) {
            Person person = (Person) player;

            Board board = person.getBoard();
            gameZone.getLeaderCardsDeck().assignCards(board);
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

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public ArrayList<Player> getPlayers() {
        synchronized (players) {
            return (ArrayList<Player>) players.clone();
        }
    }

    public GameZone getGameZone() {
        return gameZone;
    }

    public void endGame() {
        gameEnded = true;
        // TODO: compute points
    }

    public void removePlayer(String nickname) throws GameAlreadyStartedException {
        if(gameStarted) {
            throw new GameAlreadyStartedException();
        }
        synchronized (players) {
            for(Player player: players) {
                Person person = (Person) player;
                if(person.getNickname().equals(nickname)) {
                    --numberOfPlayers;
                    players.remove(person);
                    if(person.isHost()) {
                        setNewHost();
                    }
                }
            }
        }
    }

    private void setNewHost() {
        synchronized (players) {
            if(players.size() == 0) {
                return;
            }
            Person person = (Person) players.get(0);
            person.setHost();
        }
    }
}
