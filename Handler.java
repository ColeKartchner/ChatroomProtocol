import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Handler
{
    /**
     * this method is invoked by a separate thread
     */
    public static final int BUFFER_SIZE = 256;

    public void process(Socket client, ArrayList clients, ArrayList clientUsernames) throws java.io.IOException {
        DataOutputStream toClient = null;
        BufferedInputStream fromChatscreen = null;
        int count = 0;

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            // This toClient will write back whatever numberCode the username produces back to ChatScreen
            toClient = new DataOutputStream(client.getOutputStream());
            // This fromChatscreen reads in the username sent by ChatScreen
            // fromChatscreen = new BufferedReader();
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            fromChatscreen = new BufferedInputStream(client.getInputStream());


            String username = fromClient.readLine();
            System.out.println(username);
            int numBytes;

            //clients.add adds the clients into their own arraylist
            clients.add(client);

            while (true) {
                String message = "[" + count + clients + fromChatscreen + "]";
                BufferedWriter broadcast;
                toClient.writeBytes(message);

                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie) { }

                count++;
            }
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
        finally {
            // close streams and socket
            if (toClient != null)
                toClient.close();
        }
    }
}