package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.NicknameSelection;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final String hostname;
    private final int port;
    private ClientState clientState;
    private String nickname;
    private PrintWriter out;
    private Socket socket;
    private LocalModel localModel;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public synchronized void handleServerMessage(String line) {
        clientState.handleServerMessage(this, line);
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

    public void setState(ClientState clientState) {
        this.clientState = clientState;
    }

    public void setModel(LocalModel localModel) {
        this.localModel = localModel;
    }

    public LocalModel getModel() {
        return localModel;
    }

    public void write(String message) {
        out.println(message);
    }

    public void openServerCommunication() throws IOException {
        socket = new Socket(hostname, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        (new Thread(new ClientServerCommunication(this, socket))).start();
    }

    public void closeServerCommunication() {
        try {
            out = null;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
