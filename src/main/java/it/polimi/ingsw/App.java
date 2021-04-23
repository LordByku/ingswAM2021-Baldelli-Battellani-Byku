package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

public class App {
    private static void printUsage() {
        System.out.println("To launch client:");
        System.out.println("App client hostname port");
        System.out.println("To launch server:");
        System.out.println("App server port");
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            printUsage();
            return;
        }

        String mode = args[0];
        if(mode.equals("server")) {
            if(args.length != 2) {
                printUsage();
                return;
            }

            int port = Integer.parseInt(args[1]);

            Server server = new Server(port);

            server.start();
        } else if(mode.equals("client")) {
            if(args.length  != 3) {
                printUsage();
                return;
            }

            String hostname = args[1];
            int port = Integer.parseInt(args[2]);

            Client client = new Client(hostname, port);

            client.start();
        } else {
            printUsage();
        }
    }
}
