package Server;

import Client.MessageCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader input;
    private PrintWriter output;

    private GameRoom currRoom = null;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }


    @Override
    public void run() {
        try {
            String request;
            while (true) {
                request = input.readLine();

                if(request.equals(MessageCommand.REQUEST_ROOMS.toString())) {
                    handleRoomsAndModesRequest();
                } else if(request.startsWith("JOIN_ROOM:")) {
                    handleJoinRoomRequest(request);
                }else if(request.startsWith("SEND_CHOICE:")) {
                    if(currRoom != null) {
                        currRoom.makeMove(request);
                    }
                } else {
                    System.out.println("Invalid request from client.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(currRoom != null) {
                currRoom.leaveRoom(this);
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRoomsAndModesRequest() {
        // generate string with rooms and modes
        StringBuilder response = new StringBuilder();
        for (GameRoom room : server.getRooms()) {
            response.append(room.getRoomName()).append(" ").append(room.getAvailableSlots()).append(", ");
        }
        output.println(response);
    }

    private void handleJoinRoomRequest(String request) {
        // JOIN_ROOM: 1
        try {
            String[] parts = request.split(":");
            int roomId = Integer.parseInt(parts[1].trim());

            if(roomId >= 1 & roomId <= 3) {
                GameRoom room = server.getRooms()[roomId-1];
                room.joinRoom(this);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            output.println("INVALID_ROOM");
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    // listen for client response
    public String listenForClientResponse() {
        try {
            return input.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrRoom(GameRoom room) {
        currRoom = room;
    }
}
