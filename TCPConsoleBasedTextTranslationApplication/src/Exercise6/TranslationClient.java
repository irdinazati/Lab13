package Exercise6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TranslationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int TIMEOUT = 5000; // Timeout value in milliseconds

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            socket.setSoTimeout(TIMEOUT); // Set the socket timeout

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    System.out.print("Enter the English text: ");
                    String englishText = consoleReader.readLine();

                    System.out.print("Enter the target language (English, Bahasa Malaysia, Arabic, Korean): ");
                    String targetLanguage = consoleReader.readLine();

                    writer.println(englishText);
                    writer.println(targetLanguage);

                    String translatedText = reader.readLine();
                    System.out.println("Translated text: " + translatedText);
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection timed out. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
