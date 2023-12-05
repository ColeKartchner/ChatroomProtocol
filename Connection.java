import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable
{
    private final ArrayList clients;

    private final ArrayList clientUsernames;

    private final ArrayList messageQueue;

    private final ArrayList dataOutputList;
    private Socket	client;
    private static Handler handler = new Handler();

    public Connection(Socket client, ArrayList clients, ArrayList clientUsernames, ArrayList messageQueue, ArrayList dataOutputList) {
        this.client = client;
        this.clients = clients;
        this.clientUsernames = clientUsernames;
        this.messageQueue = messageQueue;
        this.dataOutputList = dataOutputList;

    }

    /**
     * This method runs in a separate thread.
     */
    public void run() {
        try {
            handler.process(client, clients, clientUsernames, messageQueue, dataOutputList);
        }
        catch (java.io.IOException ioe) {
            System.err.println(ioe);
        }
    }
}