import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    // private List<ClientHandler> clientHandlerList;

    public Server() {
        // clientHandlerList = new ArrayList<>();
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Listening ...");
            while (true) {
                /*
                    TODO: Uncomment if/when notification is necessary
                    ClientHandler newClientHandler = new ClientHandler(serverSocket.accept());
                    clientHandlerList.add(newClientHandler);
                    newClientHandler.start();
                */
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Oh crap.");
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private static int activeUserCount = 0;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void incrementUserCount() {
            activeUserCount += 1;
        }

        public void decrementUserCount() {
            activeUserCount -= 1;
        }

        public void run() {

            System.out.println("Oh, new blood!");
            incrementUserCount();

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Log the client text
                    System.out.println("A client said the following:" + "\t" + inputLine);
                    out.println(String.format("Server[%d]: \"%s\"", activeUserCount, inputLine));

                    if (".".equals(inputLine)) {
                        System.out.println("Bye, friend.");
                        decrementUserCount();
                        clientSocket.close(); // may be redundant
                        break;
                    }
                }

                System.out.println("Closing socket things then");
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(4444);
    }

}