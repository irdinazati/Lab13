package Exercise3;

import java.io.*;
import java.net.*;

public class Client {
    private String ipAddress;
    private int port;

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void connect() {
        try {
            Socket socket = new Socket(ipAddress, port);
            System.out.println("Connected to server: " + socket.getInetAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String text = "This is a sample text."; // Replace with your desired text
            writer.write(text);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            System.out.println("Word count received from server: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("10.200.122.73", 8080); 
        client.connect();
    }
}
