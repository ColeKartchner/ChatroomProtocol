/**
 * An annoying server listening on port 6008.
 *
 * @author - Greg Gagne.
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class AnnoyingServer
{
    public static final int DEFAULT_PORT = 5045;

    // construct a thread pool for concurrency
    private static final Executor exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        ServerSocket sock = null;

        try {
            // establish the socket
            sock = new ServerSocket(DEFAULT_PORT);
            // Create an array list that will hold each user in it, allowing you to
            // iterate through the clients to write message to all of them
            ArrayList<String> clients = new ArrayList<String>();
            ArrayList<String> clientUsernames = new ArrayList<String>();

            while (true) {
                /**
                 * now listen for connections
                 * and service the connection in a separate thread.
                 */
                Runnable task = new Connection(sock.accept(), clients, clientUsernames);
                System.out.println(task);
                exec.execute(task);
            }
        }
        catch (IOException ioe) { System.err.println(ioe); }
        finally {
            if (sock != null)
                sock.close();
        }
    }
}

