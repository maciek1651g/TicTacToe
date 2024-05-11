package Client;

import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public Menu() {
        System.out.println("Welcome to the TicTacToe!");
        // provide server address ip
    }

    public String init() {
        System.out.print("Enter server address [127.0.0.1]: ");
        String serverAddress = scanner.nextLine();

        if(serverAddress.isEmpty()) {
            return "127.0.0.1";
        }
        return serverAddress;
    }

    public int showMenu() {
        System.out.println("1. Show rooms");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return -1;
        }
    }

    public Integer showRoomsAndSelect(String rooms) {
        System.out.println("Rooms:");
        System.out.println(rooms);

        while (true) {
            try {
                System.out.print("Enter room number: ");
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public Integer makeChoiceForX() {
        // provide index of row and validate input
        while (true) {
            try {
                System.out.print("Provide index of row (0, 1, 2): ");
                var row = scanner.nextInt();
                if (row >= 0 && row <= 2) {
                    return row;
                } else {
                    throw new Exception();
                }
            } catch (Exception ignored) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public Integer makeChoiceForY() {
        while (true) {
            try {
                System.out.print("Provide index of column (0, 1, 2): ");
                var col = scanner.nextInt();
                if (col >= 0 && col <= 2) {
                    return col;
                } else {
                    throw new Exception();
                }
            } catch (Exception ignored) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
