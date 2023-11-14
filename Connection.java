import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable
{
    private final ArrayList clients;
    private Socket	client;
    private static Handler handler = new Handler();

    public Connection(Socket client, ArrayList clients) {
        this.client = client;
        this.clients = clients;
    }



    /**
     * This method runs in a separate thread.
     */
    public void run() {
        try {
            handler.process(client, clients);
        }
        catch (java.io.IOException ioe) {
            System.err.println(ioe);
        }
    }
}