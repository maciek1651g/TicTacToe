import Client.ClientActor;

public class Main {
    public static void main(String[] args) {
        // run server or client based on args
        if (args.length == 0) {
            System.out.println("Please provide 'server' or 'client' as an argument.");
            System.exit(1);
        } else {
            switch (args[0]) {
                case "server":
                    Server.ServerActor.main(args);
                    break;
                case "client":
                    ClientActor.main(args);
                    break;
                default:
                    System.out.println("Invalid argument. Please provide 'server' or 'client'.");
                    System.exit(1);
            }
        }
    }
}