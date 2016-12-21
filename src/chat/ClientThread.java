package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONObject;

/**
 *
 * @author João Leite 8140453
 */
public class ClientThread extends Thread {

  private final Socket socket;
  private boolean authenticated = false;
  private final ChatProtocol protocol = new ChatProtocol();
  private String user = new String();
  private final BufferedReader in;
  private final PrintWriter out;

  BlockingQueue<PrintWriter> queue = new LinkedBlockingQueue<>();
  SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");

  public ClientThread(Socket socket, BlockingQueue<PrintWriter> queue) throws IOException {
    super();
    this.queue = queue;
    this.socket = socket;
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void doAuth() throws SQLException, IOException {
    String inputLine, answer;
    out.println("Welcome to the best chat of the world (not)  [/login to authenticate or /register to register]");
    while (authenticated == false) {
      if ((inputLine = in.readLine()) != null) {
        System.out.println("Received from client" + "[" + socket.getPort() + "] -> " + inputLine);
        JSONObject message = new JSONObject(inputLine);
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
  }

  public void run() {

    try {

      String inputLine;
      JSONObject message = null;

      doAuth();

      while ((inputLine = in.readLine()) != null) {
        System.out.println("Received from " + user + "[" + socket.getPort() + "] -> " + inputLine);
        message = new JSONObject(inputLine);

        if (message.isNull("message") == false) {

          for (PrintWriter outStream : queue) {
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
