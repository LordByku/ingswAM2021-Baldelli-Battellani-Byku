package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.LocalController;
import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.Lobby;
import it.polimi.ingsw.network.client.clientStates.Welcome;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.localModel.LocalModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Client {
    private final String hostname;
    private final int port;
    private final int timerDelay = 10000;
    private ClientState clientState;
    private String nickname;
    private PrintWriter serverOut;
    private Socket socket;
    private LocalModel localModel;
    private boolean singlePlayer;
    private BlockingQueue<String> readBuffer;
    private BlockingQueue<String> writeBuffer;
    private Thread clientUserCommunication;
    private Thread localController;
    private Thread localClientCommunication;
    private ViewInterface viewInterface;
    private final BufferedReader stdin;
    private final boolean guiSelection;

    public Client(String hostname, int port, boolean guiSelection) {
        this.hostname = hostname;
        this.port = port;
        nickname = null;
        singlePlayer = false;
        stdin = new BufferedReader(new InputStreamReader(System.in));
        this.guiSelection = guiSelection;
    }

    public synchronized void handleServerMessage(String line) {
        if (line.equals("ping")) {
            write("pong");
        } else {
            clientState.handleServerMessage(this, line);
        }
    }

    public synchronized void handleUserMessage(String line) {
        clientState.handleUserMessage(this, line);
    }

    public void start() {
        if(guiSelection) {
            viewInterface = new GUI(this);
        } else {
            viewInterface = new CLI();
        }
        clientState = new Welcome(viewInterface);
        clientUserCommunication = new Thread(new ClientUserCommunication(this, stdin));
        clientUserCommunication.start();
        viewInterface.init(this);

        try {
            clientUserCommunication.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean isSinglePlayer() {
        return singlePlayer;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public void setState(ClientState clientState) {
        this.clientState = clientState;
    }

    public LocalModel getModel() {
        return localModel;
    }

    public void setModel(LocalModel localModel) {
        this.localModel = localModel;
    }

    public void write(String message) {
        if (singlePlayer) {
            try {
                writeBuffer.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            serverOut.println(message);
        }
    }

    public void openServerCommunication() throws IOException {
        socket = new Socket(hostname, port);
        socket.setSoTimeout(2 * timerDelay);
        serverOut = new PrintWriter(socket.getOutputStream(), true);
        (new Thread(new ClientServerCommunication(this, socket))).start();
    }

    public void closeServerCommunication() {
        try {
            serverOut = null;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (!socket.isClosed()) {
            closeServerCommunication();
        }

        try {
            if(!getModel().getEndGame()) {
                connectToServer();
            }
        } catch (IOException e) {
            // TODO: use view interface
            CLI.connectionError();
            CLI.reconnecting(timerDelay);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reconnect();
                }
            }, timerDelay);
        }
    }

    public void connectToServer() throws IOException {
        openServerCommunication();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", getNickname());
        write(jsonObject.toString());
        setState(new Lobby(viewInterface));
    }

    public void startLocalController() {
        readBuffer = new ArrayBlockingQueue<>(1);
        writeBuffer = new ArrayBlockingQueue<>(1);

        localClientCommunication = new Thread(new LocalClientCommunication(this, readBuffer));
        localClientCommunication.setDaemon(true);
        localClientCommunication.start();

        localController = new Thread(new LocalController(this, writeBuffer, readBuffer));
        localController.setDaemon(true);
        localController.start();
    }

    public void exit() {
        if (socket != null && !socket.isClosed()) {
            closeServerCommunication();
        }

        clientUserCommunication.interrupt();
    }
}
