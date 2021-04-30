package it.polimi.ingsw.network;

import it.polimi.ingsw.view.cli.CLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private final String hostname;
    private final int port;
    private final CLI cli;
    private final BufferedReader stdin;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        cli = new CLI();
        stdin = new BufferedReader(new InputStreamReader(System.in));
    }

    public void startMultiPlayer(String nickname) {
        try {
            Socket socket = new Socket(hostname, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //Scanner in = new Scanner(socket.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String userInput, socketInput;

            out.println(nickname);

            socketInput = in.readLine();

            System.out.println(socketInput);

            while((userInput = stdin.readLine()) != null) {
            }
        } catch (UnknownHostException e) {
            System.out.println("Invalid hostname: " + hostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O error with " + hostname);
            e.printStackTrace();
        }
    }

    public void startSinglePlayer(String nickname) {

    }

    public void start() {
        String userInput;

        cli.welcome();
        try {
            userInput = stdin.readLine();
            String nickname = userInput;

            int mode;
            do {
                cli.selectMode();
                userInput = stdin.readLine();
                try {
                    mode = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    mode = 0;
                }
            } while(mode != 1 && mode != 2);

            if(mode == 1) {
                startMultiPlayer(nickname);
            } else {
                startSinglePlayer(nickname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
