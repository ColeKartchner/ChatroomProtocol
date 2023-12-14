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

            screen.displayMessage("Reader thread good");
            while (true) {
                String message = fromServer.readLine();
                System.out.println(message);
                broadcastChecker(message);
                // now display it on the display area
                // screen.displayMessage(message);

            }
        }
        catch (IOException ioe) { System.out.println(ioe); }

    }
    private void broadcastChecker(String broadcastCode) {

        switch (broadcastCode) {
            case "5":
                System.out.println("Reserved characters are present in this message, try again");
                break;
            case "6":
                System.out.println("Message is too long or too short, try again");
                break;
            case "8":
                // screen.displayMessage(broadcastMessage); // Successfully sent message
                break;
            default:
                screen.displayMessage(broadcastCode);
            }
        }
    }