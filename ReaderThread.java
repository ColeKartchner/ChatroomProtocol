/**
 * This thread is passed a socket that it reads from. Whenever it gets input
 * it writes it to the ChatScreen text area using the displayMessage() method.
 */

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ReaderThread implements Runnable {
    Socket server;
    BufferedReader fromServer;
    ChatScreen screen;


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
                    System.out.println("Message length invalid");
                    break;
                case "6":
                    System.out.println("Reserved characters are present in this message, try again");
                    break;
                case "7":
                    // screen.displayMessage(broadcastMessage); // Successfully sent message
                    break;
                default:
                    screen.displayMessage(broadcastCode);
            }
        }
    }