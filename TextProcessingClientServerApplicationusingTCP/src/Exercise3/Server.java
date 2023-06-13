package Exercise3;

import java.io.*;
import java.net.*;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String text = reader.readLine();
                int wordCount = countWords(text);

                writer.write(Integer.toString(wordCount));
                writer.newLine();
                writer.flush();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countWords(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }

    public static void main(String[] args) {
        Server server = new Server(8080); 
        server.start();
    }
}
