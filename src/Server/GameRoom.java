package Server;

import Client.Choice;
import Client.GameBoard;
import Client.MessageCommand;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private String roomName;
    private List<ClientHandler> players;
    private boolean isGameStarted = false;

    private GameBoard gameBoard;
    private int turn;
    private ClientHandler[] currPlayers;

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

    public void joinRoom(ClientHandler player) {
        if(!isFull()) {
            players.add(player);
            player.sendMessage(MessageCommand.ROOM_JOINED.toString());
            player.setCurrRoom(this);

            if(isFull()) {
                startGame();
            }
        } else {
            player.sendMessage("ROOM_FULL");
        }
    }

    private void startGame() {
        isGameStarted = true;

        // sent to all players that game is starting
        var player1 = players.get(0);
        var player2 = players.get(1);

        player1.sendMessage("GAME_START: X");
        player2.sendMessage("GAME_START: O");

        turn = 0;
        currPlayers = new ClientHandler[]{player1, player2};
        gameBoard = new GameBoard();
    }

    public void makeMove(String response) {
        if(isGameStarted) {
            Choice choice = null;
            int x = -1;
            int y = -1;
            try {
                // "SEND_CHOICE: choice=X x=1 y=1"
                String[] parts = response.split(" ");
                for (String part : parts) {
                    if(part.startsWith("choice=")) {
                        choice = Choice.from(part.split("=")[1]);
                    } else if(part.startsWith("x=")) {
                        x = Integer.parseInt(part.split("=")[1]);
                    } else if(part.startsWith("y=")) {
                        y = Integer.parseInt(part.split("=")[1]);
                    }
                }

                if(x >= 0 && x <= 2 && y >= 0 && y <= 2 && choice != null) {
                    var correctMove = gameBoard.setOption(choice, x, y);
                    if(!correctMove) {
                        throw new Exception();
                    }

                    turn = (turn + 1) % 2;
                    currPlayers[turn].sendMessage(response);

                    if(gameBoard.isGameOver()) {
                        endGame();
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                currPlayers[turn].sendMessage(MessageCommand.INVALID_MOVE.toString());
            }
        }
    }

    private void endGame() {
        var winner = gameBoard.findWinner();
        if(winner == null) {
            currPlayers[0].sendMessage("GAME_OVER: 2");
            currPlayers[1].sendMessage("GAME_OVER: 2");
        } else {
            currPlayers[0].sendMessage("GAME_OVER: " + (winner == Choice.X ? 1 : 0));
            currPlayers[1].sendMessage("GAME_OVER: " + (winner == Choice.O ? 1 : 0));
        }

        isGameStarted = false;
        clearRoom();
    }

    private void clearRoom() {
        gameBoard = null;
        currPlayers = null;
        turn = 0;
        for(var player : players) {
            player.setCurrRoom(null);
        }
        players.clear();
    }

    private void interruptGame() {
        isGameStarted = false;
        for(var player : players) {
            player.sendMessage(MessageCommand.GAME_INTERRUPTED.toString());
        }
        clearRoom();
    }

    public void leaveRoom(ClientHandler player) {
        players.remove(player);
        player.setCurrRoom(null);
        if(isGameStarted) {
            interruptGame();
        }
    }
}
