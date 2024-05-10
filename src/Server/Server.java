package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    GameRoom[] rooms ;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started.");

        rooms = new GameRoom[3];
        rooms[0] = new GameRoom("Room 1");
        rooms[1] = new GameRoom("Room 2");
        rooms[2] = new GameRoom("Room 3");
    }


    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, this)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public GameRoom[] getRooms() {
        return rooms;
    }
}
