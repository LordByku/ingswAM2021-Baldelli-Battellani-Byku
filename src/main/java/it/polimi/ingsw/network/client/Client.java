package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.LocalController;
import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.Lobby;
import it.polimi.ingsw.network.client.clientStates.NicknameSelection;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.LocalModel;

import java.io.IOException;
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
    private Boolean singlePlayer;
    private BlockingQueue<String> readBuffer;
    private BlockingQueue<String> writeBuffer;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        nickname = null;
        singlePlayer = null;
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
        CLI.getInstance().welcome();
        clientState = new NicknameSelection();
        Thread thread = new Thread(new ClientUserCommunication(this));
        thread.start();
        try {
            thread.join();
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
        serverOut = new PrintWriter(socket.getOutputStream(), true);
        socket.setSoTimeout(2 * timerDelay);
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
            connectToServer();
        } catch (IOException e) {
            CLI.getInstance().connectionError();
            CLI.getInstance().reconnecting(timerDelay);
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
        setState(new Lobby());
    }

    public void startLocalController() {
        readBuffer = new ArrayBlockingQueue<>(1);
        writeBuffer = new ArrayBlockingQueue<>(1);

        Thread localClientCommunication = new Thread(new LocalClientCommunication(this, readBuffer));
        localClientCommunication.start();

        Thread controller = new Thread(new LocalController(this, writeBuffer, readBuffer));
        controller.start();
    }
}
