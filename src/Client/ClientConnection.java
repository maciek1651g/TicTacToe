package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientConnection {
    final BufferedReader localConsole = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private final Socket socket;
    private final BufferedReader serverInput;
    private final PrintWriter serverOutput;

    public ClientConnection(final String serverAddress, final int serverPort) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.serverInput = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.serverOutput = new PrintWriter(this.socket.getOutputStream(), true, StandardCharsets.UTF_8);
    }

    public String listenForServerResponse() {
        try {
            return serverInput.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRooms() {
        this.serverOutput.println(MessageCommand.REQUEST_ROOMS);
        try {
            return serverInput.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean joinRoom(final String roomId) {
        this.serverOutput.println("JOIN_ROOM: " + roomId);
        try {
            return serverInput.readLine().equals("ROOM_JOINED");
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendChoice(final Choice choice, int x, int y) {
        this.serverOutput.println("SEND_CHOICE: choice=%s x=%d y=%d".formatted(choice, x, y));
    }

    public void close() {
        try {
            this.socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
