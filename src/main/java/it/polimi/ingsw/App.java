package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;

public class App {
    private static void printUsage() {
        System.out.println("To launch client:");
        System.out.println("java -jar AM24.jar client <hostname> <port> <cli/gui>");
        System.out.println("To launch server:");
        System.out.println("java -jar AM24.jar server <port>");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String mode = args[0];
        if (mode.equals("server")) {
            if (args.length != 2) {
                printUsage();
                return;
            }

            int port = Integer.parseInt(args[1]);

            Server server = new Server(port);

            server.start();
        } else if (mode.equals("client")) {
            if (args.length != 4) {
                printUsage();
                return;
            }

            String hostname = args[1];
            int port = Integer.parseInt(args[2]);

            boolean guiSelection;
            switch (args[3]) {
                case "cli": {
                    guiSelection = false;
                    break;
                }
                case "gui": {
                    guiSelection = true;
                    break;
                }
                default: {
                    printUsage();
                    return;
                }
            }

            Client client = new Client(hostname, port, guiSelection);

            client.start();
        } else {
            printUsage();
        }
    }
}
