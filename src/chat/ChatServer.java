package chat;

/**
 * @author João Leite 8140453
 */
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        int portNumber = 2017;
        boolean listening = true;
        BlockingQueue<PrintWriter> queue = new LinkedBlockingQueue<PrintWriter>();
        List<Socket> sockets = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(portNumber);

            while (listening) {
                Socket newClient = serverSocket.accept();
                System.out.println("New connection! [" + newClient.getRemoteSocketAddress() + "][" + newClient.getPort()+ "]");

                sockets.add(newClient);
                
                new ClientThread(newClient, queue).start();
            }
        } catch (IOException e) {
            System.err.println("Couldn't not listen on port: " + portNumber);
            System.exit(-1);
        }
    }
}
