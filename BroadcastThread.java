import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BroadcastThread implements Runnable
{
    ArrayList<String> messageQueue;
    ArrayList<DataOutputStream> dataOutputList;

    public BroadcastThread(ArrayList messageQueue, ArrayList dataOutputList) {
        this.messageQueue = messageQueue;
        this.dataOutputList = dataOutputList;
    }

    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }
                try {
                    while (!messageQueue.isEmpty()) {
                        String currentString = (String) messageQueue.get(0);
                        messageQueue.remove(0);
                        for (int j = 0; j < dataOutputList.size(); j++) {
                            DataOutputStream currentData = dataOutputList.get(j);
                            System.out.println("Connection made, " + j);
                            currentData.writeBytes(currentString + "\n");
                        }
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }

            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
        }
    }
}
