/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards 
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;


public class ChatScreen extends JFrame implements ActionListener, KeyListener
{
    private JButton sendButton;
    private JButton exitButton;
    private JTextField sendText;
    private JTextArea displayArea;
    private Socket server;
    private PrintWriter writer;
    private BufferedReader reader;

    public static final int PORT = 5045;

    public ChatScreen() {
        /**
         * a panel used for placing components
         */
        JPanel p = new JPanel();

        Border etched = BorderFactory.createEtchedBorder();
        Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
        p.setBorder(titled);

        /**
         * set up all the components
         */
        sendText = new JTextField(30);
        sendButton = new JButton("Send");
        exitButton = new JButton("Exit");

        /**
         * register the listeners for the different button clicks
         */
        sendText.addKeyListener(this);
        sendButton.addActionListener(this);
        exitButton.addActionListener(this);

        /**
         * add the components to the panel
         */
        p.add(sendText);
        p.add(sendButton);
        p.add(exitButton);

        /**
         * add the panel to the "south" end of the container
         */
        getContentPane().add(p,"South");

        /**
         * add the text area for displaying output. Associate
         * a scrollbar with this text area. Note we add the scrollpane
         * to the container, not the text area
         */
        displayArea = new JTextArea(15,40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        getContentPane().add(scrollPane,"Center");

        /**
         * set the title and size of the frame
         */
        setTitle("GUI Demo");
        pack();

        setVisible(true);
        sendText.requestFocus();

        /** anonymous inner class to handle window closing events */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        } );

    }

    /**
     * Displays a message
     */
    public void displayMessage(String message) {
        displayArea.append(message + "\n");
    }

    /**
     * This gets the text the user entered and outputs it
     * in the display area.
     */
    public void displayText() {
        String message = sendText.getText().trim();
        StringBuffer buffer = new StringBuffer(message.length());

        // now reverse it
        for (int i = message.length()-1; i >= 0; i--)
            buffer.append(message.charAt(i));

        displayArea.append(buffer.toString() + "\n");

        sendText.setText("");
        sendText.requestFocus();
    }


    /**
     * This method responds to action events .... i.e. button clicks
     * and fulfills the contract of the ActionListener interface.
     */
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == sendButton)
            displayText();
        else if (source == exitButton)
            System.exit(0);
    }

    /**
     * These methods responds to keystroke events and fulfills
     * the contract of the KeyListener interface.
     */

    /**
     * This is invoked when the user presses
     * the ENTER key.
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            displayText();
    }

    /** Not implemented */
    public void keyReleased(KeyEvent e) { }

    /** Not implemented */
    public void keyTyped(KeyEvent e) {  }


    public static void main(String[] args) {
        try {
            Socket server = new Socket(args[0], PORT);
            // Writer that converts args[1] to the handler, so we can check it
            PrintWriter writer = new PrintWriter(server.getOutputStream(), true);
            // Reader that reads in the input returned from handler
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            // This is what actually writes to the Handler
            writer.println("user<" + args[1] + ">");
            String usernameCode = reader.readLine();
            System.out.println(usernameCode);
            String parsedUsername = reader.readLine();
            System.out.println(parsedUsername);
            // This reads in the code(1, 2, 3, or 4 returned from handler)

            // if handler.java returns 1, means username is taken
            if (usernameCode.equals("1")) {
                System.out.println("That Username is already taken, try again");
            }

            if (usernameCode.equals("2")) {
                System.out.println("Reserved characters are present in this Username, try again");
            }

            if (usernameCode.equals("3")) {
                System.out.println("Username must be less than 20 characters");
            }

            if (usernameCode.equals("4")) {
                System.out.println("Success!");

                // This creates the chatscreen UI
                ChatScreen win = new ChatScreen();
                win.displayMessage("My name is " + parsedUsername);
                win.displayMessage("To leave the chat type 'EXIT', " +
                        "if you want to type a private message type PRIVATE 'username'");

                Thread ReaderThread = new Thread(new ReaderThread(server, win));

                ReaderThread.start();

                writer.println("broadcast<" + args[1] + ",1:00," + ReaderThread + ">");

                String messageCode = reader.readLine();

                if (messageCode.equals("5")) {
                    System.out.println("Message can't be < 0 characters and can't be > 1024 characters");
                }

                if (messageCode.equals("6")) {
                    System.out.println("Message has reserved characters in it");
                }

                if (messageCode.equals("7")) {
                    System.out.println("Good message lad");
                    ReaderThread.start();
                }
            }
        }
        catch (UnknownHostException uhe) {
                System.out.println(uhe);
            }

        catch (IOException ioe) {
            System.out.println(ioe); }


    }
}