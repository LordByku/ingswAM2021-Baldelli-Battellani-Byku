package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.gameZone.GameZone;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.parsing.InitGameParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Game {
    private static Game instance;
    private final ArrayList<Player> players;
    private GameZone gameZone;
    private int numberOfPlayers;
    private volatile boolean gameStarted;
    private volatile boolean gameEnded;
    private volatile boolean isLastTurn;
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
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    // for tests only
    public static void reset() {
        instance = new Game();
    }

    public Player addPlayer(String nickname)
            throws FullLobbyException, ExistingNicknameException, InvalidNicknameException,
            GameAlreadyStartedException, GameEndedException {
        synchronized (players) {
            if(gameEnded) {
                throw new GameEndedException();
            }
            if (gameStarted) {
                for (Player player : players) {
                    if (player.getPlayerType() == PlayerType.PERSON) {
                        Person person = (Person) player;
                        if (person.getNickname().equals(nickname) && !person.isConnected()) {
                            person.reconnect();
                            return person;
                        }
                    }
                }
                throw new GameAlreadyStartedException();
            }
            if (players.size() == 4) {
                throw new FullLobbyException();
            }
            for (Player player : players) {
                Person person = (Person) player;
                if (person.getNickname().equals(nickname)) {
                    throw new ExistingNicknameException();
                }
            }

            Person person = new Person(nickname);
            players.add(person);
            if (++numberOfPlayers == 1) {
                setNewHost();
            }
            return person;
        }
    }

    public void startMultiPlayer() throws NotEnoughPlayersException, GameAlreadyStartedException {
        if (gameStarted) {
            throw new GameAlreadyStartedException();
        }

        synchronized (players) {
            if (players.size() < 2) {
                throw new NotEnoughPlayersException();
            }

            Collections.shuffle(players);

            for (int i = 0; i < players.size(); i++) {
                Person person = (Person) players.get(i);

                Board board = person.getBoard();
                gameZone.getLeaderCardsDeck().assignCards(board);
                int receivedFaithPoints = InitGameParser.getInstance().getInitFaithPoints(i);
                int receivedResources = InitGameParser.getInstance().getInitResources(i);
                board.getFaithTrack().addFaithPoints(receivedFaithPoints);
                if (receivedResources == 0) {
                    person.initSelectDone();
                }
            }

            VRSObserver.getInstance().updateVRS();

            gameStarted = true;
            currentPlayer = 0;
            players.get(currentPlayer).startTurn();
        }
    }

    public Person getSinglePlayer() {
        synchronized (players) {
            return (Person) players.get(0);
        }
    }

    public Computer getComputer() {
        synchronized (players) {
            return (Computer) players.get(1);
        }
    }

    public void startSinglePlayer() throws NotEnoughPlayersException, GameAlreadyStartedException {
        if (gameStarted) {
            throw new GameAlreadyStartedException();
        }

        synchronized (players) {
            if (players.size() != 1) {
                throw new NotEnoughPlayersException();
            }

            Board board = getSinglePlayer().getBoard();
            gameZone.getLeaderCardsDeck().assignCards(board);
            getSinglePlayer().initSelectDone();

            players.add(new Computer());

            gameStarted = true;
            currentPlayer = 0;
            players.get(currentPlayer).startTurn();
        }
    }

    public void setLastTurn() throws GameNotStartedException, GameEndedException {
        if (!gameStarted) {
            throw new GameNotStartedException();
        }
        if (gameEnded) {
            throw new GameEndedException();
        }
        isLastTurn = true;
    }

    protected void handleTurnOrder() throws GameNotStartedException, GameEndedException {
        if (!gameStarted) {
            throw new GameNotStartedException();
        }
        if (gameEnded) {
            throw new GameEndedException();
        }
        synchronized (players) {
            currentPlayer = (currentPlayer + 1) % players.size();
            if (isLastTurn && currentPlayer == 0) {
                endGame();
            } else {
                players.get(currentPlayer).startTurn();
            }
        }
    }

    public int getNumberOfPlayers() {
        synchronized (players) {
            return numberOfPlayers;
        }
    }

    public ArrayList<Player> getPlayers() {
        synchronized (players) {
            return (ArrayList<Player>) players.clone();
        }
    }

    public GameZone getGameZone() {
        return gameZone;
    }

    private void endGame() {
        gameEnded = true;
    }

    public void removePlayer(String nickname) throws GameAlreadyStartedException {
        if (gameStarted) {
            throw new GameAlreadyStartedException();
        }
        synchronized (players) {
            for (Iterator<Player> iterator = players.iterator(); iterator.hasNext(); ) {
                Person person = (Person) iterator.next();
                if (person.getNickname().equals(nickname)) {
                    --numberOfPlayers;
                    iterator.remove();
                    if (person.isHost()) {
                        setNewHost();
                    }
                }
            }
        }
    }

    private void setNewHost() {
        synchronized (players) {
            if (players.size() == 0) {
                return;
            }
            Person person = (Person) players.get(0);
            person.setHost();
        }
    }

    public boolean hasGameStarted() {
        return gameStarted;
    }

    public int getPlayerIndex(Person person) {
        synchronized (players) {
            return players.indexOf(person);
        }
    }

    public boolean allDisconnected() {
        synchronized (players) {
            for (Player player : players) {
                if (player.getPlayerType() == PlayerType.PERSON) {
                    Person person = (Person) player;
                    if (person.isConnected()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public boolean hasGameEnded() {
        return gameEnded;
    }

    public void refreshModel() {
        gameZone = new GameZone();
    }

    public Person getActivePerson() {
        synchronized (players) {
            Person activePlayer = null;
            for (Player player: players) {
                if (player.getPlayerType() == PlayerType.PERSON) {
                    Person person = (Person) player;
                    if (person.isActivePlayer()) {
                        activePlayer = person;
                    }
                }
            }
            return activePlayer;
        }
    }
}
