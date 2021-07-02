package it.polimi.ingsw.network.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.LocalController;
import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.Lobby;
import it.polimi.ingsw.network.client.clientStates.Welcome;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
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
    private final boolean guiSelection;
    private ClientState clientState;
    private String nickname;
    private PrintWriter serverOut;
    private Socket socket;
    private LocalModel localModel;
    private boolean offlineGame;
    private BlockingQueue<String> readBuffer;
    private BlockingQueue<String> writeBuffer;
    private Thread localController;
    private Thread localClientCommunication;
    private Thread clientServerCommunication;
    private ViewInterface viewInterface;

    public Client(String hostname, int port, boolean guiSelection) {
        this.hostname = hostname;
        this.port = port;
        nickname = null;
        offlineGame = false;
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
        if (guiSelection) {
            viewInterface = new GUI(this);
        } else {
            viewInterface = new CLI(this);
        }
        clientState = new Welcome(viewInterface);
        viewInterface.init();
        viewInterface.join();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setOfflineGame(boolean offlineGame) {
        this.offlineGame = offlineGame;
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
        if (offlineGame) {
            try {
                writeBuffer.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if(serverOut != null) {
                serverOut.println(message);
            }
        }
    }

    public void openServerCommunication() throws IOException {
        viewInterface.startConnection();
        System.out.println("creating socket");
        socket = new Socket(hostname, port);
        socket.setSoTimeout(3 * timerDelay);
        serverOut = new PrintWriter(socket.getOutputStream(), true);
        clientServerCommunication = new Thread(new ClientServerCommunication(this, socket));
        clientServerCommunication.start();
        System.out.println("communication thread started");
    }

    public void closeServerCommunication() {
        try {
            serverOut = null;
            clientServerCommunication.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (!socket.isClosed()) {
            closeServerCommunication();
        }

        System.out.println("socket closed? " + socket.isClosed());

        try {
            if (getModel() == null || !getModel().getEndGame()) {
                connectToServer();
            }
        } catch (IOException e) {
            viewInterface.connectionFailed(timerDelay);
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reconnect();
                }
            }, timerDelay);
        }
    }

    public void connectToServer() throws IOException {
        System.out.println("connecting to server");
        openServerCommunication();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", getNickname());
        System.out.println("sending: " + jsonObject.toString());
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

        viewInterface.terminate();
    }

    public JsonObject buildRequestMessage(CommandType commandType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "newCommand");
        jsonObject.add("command", JsonUtil.getInstance().serialize(commandType));
        return jsonObject;
    }

    public JsonObject buildCommandMessage(String command, JsonElement value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "action");
        jsonObject.addProperty("command", command);
        jsonObject.add("value", value);
        return jsonObject;
    }

    public JsonObject buildCancelMessage() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "cancel");
        return jsonObject;
    }

    public JsonObject buildEndTurnMessage() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "endTurn");
        return jsonObject;
    }
}
