package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONObject;

/**
 *
 * @author João Leite 8140453
 */
public class ClientThread extends Thread {

    private Socket socket = null;
    private boolean authenticated = false;
    private ChatProtocol protocol = new ChatProtocol();
    private String user = new String();
    BlockingQueue<PrintWriter> queue = new LinkedBlockingQueue<PrintWriter>();
    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");

    public ClientThread(Socket socket, BlockingQueue<PrintWriter> queue) {
        super();
        this.queue = queue;
        this.socket = socket;
    }

    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String inputLine, answer;
            JSONObject message = null;

            out.println("Welcome to the best chat of the world (not)  [/login to authenticate or /register to register]");
            while (authenticated == false) {
                if ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client" + "[" + socket.getPort() + "] -> " + inputLine);
                    message = new JSONObject(inputLine);
                    if (message.has("command")) {
                        answer = protocol.processAuth(message);
                        out.println(answer);
                        if (answer.equals("Login Successful!")) { // if login successfull
                            user = message.getString("username");
                            authenticated = true;
                        }
                    } else {
                        out.println("You have to use /login or /register command");
                    }
                }
            }
            // AFTER AUTHENTICATION
            queue.add(out);

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + user + "[" + socket.getPort() + "] -> " + inputLine);
                message = new JSONObject(inputLine);
                
                if (message.isNull("message") == false) {
                    
                    for( PrintWriter outStream: queue){
                        String outLine = "[" + ft.format(new Date()) + " @" + user + "] -> " + message.getString("message");
                        outStream.println(outLine);
                    }
                }
            }

            in.close();
            socket.close();

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
}
