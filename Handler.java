import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Handler
{
    /**
     * this method is invoked by a separate thread
     */
    public static final int BUFFER_SIZE = 256;

    public void process(Socket client, ArrayList clients, ArrayList clientUsernames, ArrayList messageQueue, ArrayList dataOutputList) throws java.io.IOException {
        DataOutputStream toClient = null;
        BufferedInputStream fromChatscreen = null;
        int count = 0;

        try {
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                // This toClient will write back whatever numberCode the username produces back to ChatScreen
                toClient = new DataOutputStream(client.getOutputStream());
                // dataOutputList.add(toClient);
                // This fromChatscreen reads in the username sent by ChatScreen
                // fromChatscreen = new BufferedReader();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                fromChatscreen = new BufferedInputStream(client.getInputStream());
                BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                String message = fromClient.readLine();
                System.out.println(message);
                int startIndex = message.indexOf('<');
                int endIndex = message.indexOf('>');
                String command = message.substring(0, startIndex);
                long countOpenBrackets = message.chars().filter(ch -> ch == '<').count();
                long countCloseBrackets = message.chars().filter(ch -> ch == '>').count();
                long countComma = message.chars().filter(ch -> ch == ',').count();
                String parsedUser = message.substring(startIndex + 1, endIndex);

                if (command.equals("user")) {
                    if (countOpenBrackets > 1 || countCloseBrackets > 1 || countComma > 0) {
                        toClient.writeBytes("2\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else if (parsedUser.length() > 20) {
                        toClient.writeBytes("3\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else if (clientUsernames.contains(parsedUser)) {
                        toClient.writeBytes("1\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else {
                        toClient.writeBytes("4\n");
                        dataOutputList.add(toClient);
                        clientUsernames.add(parsedUser);
                        //store BWriter in an ArrayList
                        //dataOutputList.add(clientWriter, parsedUser);
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    }
                } else if (command.equals("private")) {
                    if (countOpenBrackets > 1 || countCloseBrackets > 1) {
                        toClient.writeBytes("7\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else if (parsedUser.length() > 1024) {
                        toClient.writeBytes("9\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    }
                } else if (command.equals("broadcast")) {
                    if (countOpenBrackets > 1 || countCloseBrackets > 1) {
                        toClient.writeBytes("6\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else if (parsedUser.length() > 1024) {
                        toClient.writeBytes("5\n");
                        toClient.writeBytes(parsedUser + "\n");
                        toClient.flush();
                    } else {
                        toClient.writeBytes("8\n");
                        toClient.writeBytes(parsedUser + "\n");
                        messageQueue.add(message);
                        toClient.flush();
                        System.out.println("Added message:" + message);
                    }
                } else if (command.equals("userlist")) {
                    toClient.writeBytes(clientUsernames + "\n");
                }
                else if (command.equals("ls")) {
                    String userlist = String.join(",", clientUsernames);
                    toClient.writeBytes("userlist<" + userlist + ">\n");
                }else if (command.equals("exit")) {
                    System.out.println(toClient);
                    dataOutputList.remove(toClient);
                    clientUsernames.remove(parsedUser);
                    System.out.println(dataOutputList + " " + clientUsernames);
                }
            }





/*
            int numBytes;

            //clients.add adds the clients into their own arraylist
            clientUsernames.add(parsedUser);

            while (true) {
                String outputMessage = "[" + count + clients + fromChatscreen + "]";
                BufferedWriter broadcast;
                toClient.writeBytes(message);

                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie) { }

                count++;
            }

 */
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