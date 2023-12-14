import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatScreen extends JFrame implements ActionListener, KeyListener {
    private JButton sendButton;
    private JButton exitButton;
    private JTextField sendText;
    private JTextArea displayArea;
    private Socket server;
    private PrintWriter writer;
    private BufferedReader reader;
    private String username;

    public static final int PORT = 5045;

    public ChatScreen(String host, String username) {
        this.username = username;

        JPanel p = new JPanel();
        Border etched = BorderFactory.createEtchedBorder();
        Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
        p.setBorder(titled);

        sendText = new JTextField(30);
        sendButton = new JButton("Send");
        exitButton = new JButton("Exit");

        sendText.addKeyListener(this);
        sendButton.addActionListener(this);
        exitButton.addActionListener(this);

        p.add(sendText);
        p.add(sendButton);
        p.add(exitButton);

        getContentPane().add(p, "South");

        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        getContentPane().add(scrollPane, "Center");

        setTitle("Chat Room");
        pack();
        setVisible(true);
        sendText.requestFocus();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        try {
            server = new Socket(host, PORT);
            writer = new PrintWriter(server.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            writer.println("user<" + username + ">");

            String usernameCode = reader.readLine();
            usernameChecker(usernameCode, username);

            Thread readerThread = new Thread(new ReaderThread(server, this));
            readerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void displayMessage(String message) {
        displayArea.append(message + "\n");
    }

    public void displayText() {
        String message = sendText.getText().trim();
        if (!message.isEmpty()) {
            if (message.equalsIgnoreCase("ls")) {
                displayUserList();
            }
            else {
                sendBroadcastMessage(message);
            }
            sendText.setText("");
            sendText.requestFocus();
        }
    }

    public void displayUserList() {
        writer.println("userlist<>");
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == sendButton)
            displayText();
        else if (source == exitButton)
            writer.println("exit<" + username + ">");
        System.exit(0);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            displayText();
    }

    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) { }

    private void usernameChecker(String code, String username) {
        switch (code) {
            case "1":
                System.out.println("That Username is already taken, try again");
                break;
            case "2":
                System.out.println("Reserved characters are present in this Username, try again");
                break;
            case "3":
                System.out.println("Username must be less than 20 characters");
                break;
            case "4":
                displayMessage("Connected as " + username);
                break;
            default:
                System.out.println("Unknown response code");
        }
    }

    private void sendBroadcastMessage(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(new Date());
        String broadcastMessage = "broadcast<" + username + "," + time + "," + message + ">";
        writer.println(broadcastMessage);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ChatScreen <host> <username>");
            System.exit(1);
        }
        new ChatScreen(args[0], args[1]);
    }
}