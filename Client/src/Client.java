import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    // TODO: Provide unique clientID to each client
    // private int clientID;

    public void startConnection(String ip, int port) {

        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader inputReader = new BufferedReader((new InputStreamReader(System.in)));

            System.out.println("Connected! Type your message!");

            while (true) {
                // Read message to be sent by the client
                String msgToSend = inputReader.readLine();

                out.println(msgToSend);
                System.out.println(in.readLine());

                // Close the socket connection if the user types in "."
                if (msgToSend.equals(".")) {
                    System.out.println("I'm disconnecting you, since you asked nicely.");
                    stopConnection();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method found online; why could it be returning a string at all?
    public String sendMessage(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection(InetAddress.getLocalHost().getHostName(), 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

