package Server;

import Client.Choice;
import Client.GameBoard;
import Client.MessageCommand;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private String roomName;
    private List<ClientHandler> players;

    public GameRoom(String roomName) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public String getAvailableSlots() {
        return "("+getPlayersCount()+"/2)";
    }

    public boolean isFull() {
        return getPlayersCount() == 2;
    }

    public void addPlayer(ClientHandler player) {
        if(!isFull()) {
            players.add(player);
        }

        if(isFull()) {
            new Thread(this::startGame).start();
        }
    }

    private void startGame() {
        // sent to all players that game is starting
        var player1 = players.get(0);
        var player2 = players.get(1);

        player1.sendMessage("GAME_START: X");
        player1.sendMessage(MessageCommand.MAKE_CHOICE.toString());
        player2.sendMessage("GAME_START: O");

        int turn = 0;
        var tabPlayers = new ClientHandler[]{player1, player2};
        final GameBoard gameBoard = new GameBoard();

        while (!gameBoard.containsWinner()) {
            var response = tabPlayers[turn].listenForClientResponse();

            // println("SEND_CHOICE: choice=%s x=%d y=%d".formatted(choice, x, y))
            if(response.startsWith("SEND_CHOICE:")) {
                var parts = response.split(":");
                var choice = Choice.from(parts[1].split("=")[1]);
                var x = Integer.parseInt(parts[2].split("=")[1]);
                var y = Integer.parseInt(parts[3].split("=")[1]);
                gameBoard.setOption(choice, x, y);
                turn = (turn + 1) % 2;
                tabPlayers[turn].sendMessage(response);
            }
        }

        var winner = gameBoard.findWinner();
        player1.sendMessage("GAME_OVER: " + (winner == Choice.X ? 1 : 0));
        player2.sendMessage("GAME_OVER: " + (winner == Choice.O ? 1 : 0));
        clearRoom();
    }

    private void clearRoom() {
        players.clear();
    }
}
