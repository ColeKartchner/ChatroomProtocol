import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable
{
    private final ArrayList clients;

    private final ArrayList clientUsernames;
    private Socket	client;
    private static Handler handler = new Handler();

    public Connection(Socket client, ArrayList clients, ArrayList clientUsernames) {
        this.client = client;
        this.clients = clients;
        this.clientUsernames = clientUsernames;

    }

    /**
     * This method runs in a separate thread.
     */
    public void run() {
        try {
            handler.process(client, clients, clientUsernames);
        }
        catch (java.io.IOException ioe) {
            System.err.println(ioe);
        }
    }
}