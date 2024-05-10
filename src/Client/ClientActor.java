package Client;

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

        while (!gameBoard.containsWinner()) {
            String command = clientConnection.listenForServerResponse();

            if (command.contains(MessageCommand.GAME_START.toString())) {
                String[] parts = command.split(":");
                choice = Choice.from(parts[1].trim());
            }

            if (command.contains(MessageCommand.GAME_OVER.toString())) {
                String[] parts = command.split(":");
                String result = parts[1].trim();
                System.out.println("Winner is : " + result);
            }

            if (command.contains(MessageCommand.MAKE_CHOICE.toString())) {
                makeChoice(clientConnection, menu, gameBoard, choice);
                gameBoard.printBoard();
            }

            if(command.startsWith("SEND_CHOICE:")) {
                var parts = command.split(":");
                var opponentChoice = Choice.from(parts[1].split("=")[1]);
                var x = Integer.parseInt(parts[2].split("=")[1]);
                var y = Integer.parseInt(parts[3].split("=")[1]);
                gameBoard.setOption(opponentChoice, x, y);
                gameBoard.printBoard();
                makeChoice(clientConnection, menu, gameBoard, choice);
                gameBoard.printBoard();
            }
        }
    }

    private static void makeChoice(ClientConnection clientConnection, Menu menu, GameBoard gameBoard, Choice choice) {
        int x = menu.makeChoiceForX();
        int y = menu.makeChoiceForY();
        gameBoard.setOption(choice, x, y);
        clientConnection.sendChoice(choice, x, y);
    }
}
