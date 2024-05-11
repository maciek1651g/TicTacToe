package Client;

// wprowadzenie litery zamiast cyfry psuje gre

public class ClientActor {

    public static void main(String[] args) {
        ClientConnection clientConnection = null;
        Menu menu = new Menu();
        String serverAddress = menu.init();

        // connect to server by socket
        try {
            clientConnection = new ClientConnection(serverAddress, 7890);
        } catch (Exception e) {
            System.out.println("Connection error. Please try again later.");
            System.exit(1);
        }

        while (true) {
            int choice = menu.showMenu();
            switch (choice) {
                case 1:
                    var rooms = clientConnection.getRooms();
                    Integer roomNumber = menu.showRoomsAndSelect(rooms);
                    if (clientConnection.joinRoom(roomNumber.toString())) {
                        System.out.println("Room joined successfully.");
                        System.out.println("Wait for the game to start...");
                        handleSession(clientConnection, menu);
                    } else {
                        System.out.println("Room not joined. Please try again.");
                    }
                    break;
                case 2:
                    clientConnection.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void handleSession(ClientConnection clientConnection, Menu menu) {
        final GameBoard gameBoard = new GameBoard();
        Choice choice = null;

        while (true) {
            String command = clientConnection.listenForServerResponse();

            if (command.contains(MessageCommand.GAME_START.toString())) {
                System.out.println("Game started!");
                String[] parts = command.split(":");
                choice = Choice.from(parts[1].trim());
                System.out.println("You are playing as '" + choice + "'");

                gameBoard.printBoard();
                if(choice == Choice.X) {
                    System.out.println("Your turn!");
                    makeChoice(clientConnection, menu, gameBoard, choice);
                    gameBoard.printBoard();
                    System.out.println("Wait for your opponent to make a move...");
                } else {
                    System.out.println("Wait for your opponent to make a move...");
                }
            }

            if (command.contains(MessageCommand.GAME_OVER.toString())) {
                String[] parts = command.split(":");
                String result = parts[1].trim();
                if(result.equals("2"))
                    System.out.println("It's a draw!");
                else if(result.equals("1"))
                    System.out.println("You won!");
                else
                    System.out.println("You lost!");
                System.out.println("Press any key to continue...");
                break;
            }

            if(command.startsWith("SEND_CHOICE:")) {
                Choice opponentChoice = null;
                int x = -1;
                int y = -1;
                String[] parts = command.split(" ");
                for (String part : parts) {
                    if(part.startsWith("choice=")) {
                        opponentChoice = Choice.from(part.split("=")[1]);
                    } else if(part.startsWith("x=")) {
                        x = Integer.parseInt(part.split("=")[1]);
                    } else if(part.startsWith("y=")) {
                        y = Integer.parseInt(part.split("=")[1]);
                    }
                }
                gameBoard.setOption(opponentChoice, x, y);
                gameBoard.printBoard();

                if(!gameBoard.isGameOver()) {
                    System.out.println("Your turn!");
                    makeChoice(clientConnection, menu, gameBoard, choice);
                    gameBoard.printBoard();

                    if(!gameBoard.isGameOver()) {
                        System.out.println("Wait for your opponent to make a move...");
                    }
                }
            }

            if (command.contains(MessageCommand.GAME_INTERRUPTED.toString())) {
                System.out.println("Game interrupted.");
                break;
            }

            if (command.contains(MessageCommand.INVALID_MOVE.toString())) {
                System.out.println("Invalid move. Please try again.");
                makeChoice(clientConnection, menu, gameBoard, choice);
                gameBoard.printBoard();
                System.out.println("Wait for your opponent to make a move...");
            }
        }
    }

    private static void makeChoice(ClientConnection clientConnection, Menu menu, GameBoard gameBoard, Choice choice) {
        var correctChoice = false;
        while (!correctChoice) {
            int x = menu.makeChoiceForX();
            int y = menu.makeChoiceForY();
            correctChoice = gameBoard.setOption(choice, x, y);
            if (correctChoice) {
                clientConnection.sendChoice(choice, x, y);
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
    }
}
