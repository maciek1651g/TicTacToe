package Server;

import java.io.IOException;

public class ServerActor {
    public static void main(String[] args) {
        Server server = null;

        try {
            server = new Server(7890);
            server.start();
        } catch (IOException e) {
            System.out.println("Connection error. Please try again later.");
            System.exit(1);
        }
    }
}
