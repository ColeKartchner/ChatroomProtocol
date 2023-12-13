import java.io.*;
import java.net.*;
import javax.swing.*;

public class ReaderThread implements Runnable {
    private Socket server;
    private BufferedReader fromServer;
    private ChatScreen screen;

    public ReaderThread(Socket server, ChatScreen screen) {
        this.server = server;
        this.screen = screen;
    }

    public void run() {
        try {
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

            while (true) {
                String message = fromServer.readLine();
                if (message != null) {
                    // Handle userlist response
                    if (message.startsWith("userlist<")) {
                        handleUserList(message);
                    } else if (message.startsWith("broadcast<")) {
                        broadcastChecker(message);
                    } else {
                        // Handle other messages
                        screen.displayMessage(message);
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void handleUserList(String message) {
        // Extract the user list and display it
        String userList = message.substring(message.indexOf('<') + 1, message.indexOf('>'));
        screen.displayMessage("Active Users: " + userList);
    }

    private void broadcastChecker(String broadcastCode) {

        switch (broadcastCode) {
            case "5":
                System.out.println("Message length invalid");
                break;
            case "6":
                System.out.println("Reserved characters are present in this message, try again");
                break;
            case "7":
                // screen.displayMessage(broadcastMessage); // Successfully sent message
                break;
            default:
                // screen.displayMessage(broadcastCode);
        }
    }
}
