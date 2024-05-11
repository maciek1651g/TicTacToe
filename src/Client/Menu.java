package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Menu {
    final BufferedReader localConsole = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    public Menu() {
        System.out.println("Welcome to the TicTacToe!");
        // provide server address ip
    }

    public String init() {
        System.out.print("Enter server address [127.0.0.1]: ");
        String serverAddress = null;
        try {
            serverAddress = localConsole.readLine().trim();
        } catch (IOException e) {
            System.out.println("Invalid input. Localhost will be used.");
        }

        if(serverAddress == null || serverAddress.isEmpty()) {
            return "127.0.0.1";
        }
        return serverAddress;
    }

    public int showMenu() {
        System.out.println("1. Show rooms");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
        try {
            return Integer.parseInt(localConsole.readLine());
        } catch (Exception e) {
            return -1;
        }
    }

    public Integer showRoomsAndSelect(String rooms) {
        System.out.println("Rooms:");
        System.out.println(rooms);

        while (true) {
            try {
                System.out.print("Enter room number: ");
                return Integer.parseInt(localConsole.readLine());
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
                var row = Integer.parseInt(localConsole.readLine());
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
                var col = Integer.parseInt(localConsole.readLine());
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
