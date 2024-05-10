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
        System.out.print("Enter room number: ");
        return scanner.nextInt();
    }

    public Integer makeChoiceForX() {
        System.out.println("Make choice for X: ");
        return scanner.nextInt();
    }

    public Integer makeChoiceForY() {
        System.out.println("Make choice for Y: ");
        return scanner.nextInt();
    }
}
