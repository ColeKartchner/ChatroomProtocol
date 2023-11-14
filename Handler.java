import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Handler
{

    /**
     * this method is invoked by a separate thread
     */
    public void process(Socket client, ArrayList clients, ArrayList clientUsernames) throws java.io.IOException {
        DataOutputStream toClient = null;
        int count = 0;

        try {
            toClient = new DataOutputStream(client.getOutputStream());
            clients.add(client);
            while (true) {
                String message = "[" + count + clients + "]\n";
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