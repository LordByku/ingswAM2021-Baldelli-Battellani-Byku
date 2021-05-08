package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.serverStates.InitDiscard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ArrayList<ClientHandler> clientHandlers;

    public Server(int port) {
        this.port = port;
        clientHandlers = new ArrayList<>();
    }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server ready");

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                synchronized (clientHandlers) {
                    clientHandlers.add(clientHandler);
                }
                executor.submit(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        executor.shutdown();
    }

    public void broadcast(String type, JsonObject message) {
        synchronized (clientHandlers) {
            for(ClientHandler clientHandler: clientHandlers) {
                clientHandler.ok(type, message);
            }
        }
    }

    public void startGame(){
        synchronized (clientHandlers){
            for(ClientHandler clientHandler: clientHandlers) {
                GameStateSerializer serializer = new GameStateSerializer(clientHandler.getPerson().getNickname());
                clientHandler.ok("update", serializer.gameState());
                clientHandler.setState(new InitDiscard());
            }
        }
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
    }
}