package Exercise7;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TranslationServerGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea requestLogTextArea;
    private JLabel requestCountLabel;
    private int requestCount = 0;
    private Map<String, Map<String, String>> translations;

    public TranslationServerGUI() {
        setTitle("Translation Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        requestLogTextArea = new JTextArea();
        requestLogTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(requestLogTextArea);
        add(scrollPane, BorderLayout.CENTER);

        requestCountLabel = new JLabel("Total Requests: 0");
        add(requestCountLabel, BorderLayout.SOUTH);

        initializeTranslations();
    }

    private void initializeTranslations() {
        translations = new HashMap<>();

        // English to Bahasa Malaysia translations
        Map<String, String> englishToBahasa = new HashMap<>();
        englishToBahasa.put("Good morning", "Selamat pagi");
        englishToBahasa.put("Good night", "Selamat malam");
        englishToBahasa.put("How are you?", "Apa khabar?");
        englishToBahasa.put("Thank you", "Terima kasih");
        englishToBahasa.put("Goodbye", "Selamat tinggal");
        englishToBahasa.put("What's up?", "Ada apa?");
        translations.put("Bahasa Malaysia", englishToBahasa);

        // English to Arabic translations
        Map<String, String> englishToArabic = new HashMap<>();
        englishToArabic.put("Good morning", "الخير صباح");
        englishToArabic.put("Good night", "مساؤك طاب");
        englishToArabic.put("How are you?", "حالك؟ كيف");
        englishToArabic.put("Thank you", "لك شكرا");
        englishToArabic.put("Goodbye", "السالمة مع");
        englishToArabic.put("What's up?", "أخبارك؟ ما");
        translations.put("Arabic", englishToArabic);

        // English to Korean translations
        Map<String, String> englishToKorean = new HashMap<>();
        englishToKorean.put("Good morning", "좋은 아침");
        englishToKorean.put("Good night", "안녕히 주무세요");
        englishToKorean.put("How are you?", "어떻게 지내세요?");
        englishToKorean.put("Thank you", "감사합니다");
        englishToKorean.put("Goodbye", "안녕");
        englishToKorean.put("What's up?", "뭐야?");
        translations.put("Korean", englishToKorean);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            logMessage("Server started. Waiting for client connection...");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void logMessage(String message) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(now);
        String log = "[" + timestamp + "] " + message;

        requestLogTextArea.append(log + "\n");
        requestLogTextArea.setCaretPosition(requestLogTextArea.getDocument().getLength());
    }

    private synchronized void incrementRequestCount() {
        requestCount++;
        requestCountLabel.setText("Total Requests: " + requestCount);
    }

    private class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String englishText = reader.readLine();
                String targetLanguage = reader.readLine();

                incrementRequestCount();
                logMessage("Translation requested - English: " + englishText + ", Target Language: " + targetLanguage);

                String translatedText = translateText(englishText, targetLanguage);
                logMessage("Translation result - Translated Text: " + translatedText);

                writer.println(translatedText);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String translateText(String englishText, String targetLanguage) {
            Map<String, String> translationMap = translations.get(targetLanguage);
            if (translationMap != null) {
                return translationMap.getOrDefault(englishText, "Translation not available");
            } else {
                return "Translation not available";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TranslationServerGUI serverGUI = new TranslationServerGUI();
            serverGUI.setVisible(true);
            serverGUI.start();
        });
    }
}

