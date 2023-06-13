package Exercise6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TranslationServer {
    private static final int PORT = 8080;
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        // Initialize the translations
        Map<String, String> englishToMalayTranslations = new HashMap<>();
        englishToMalayTranslations.put("Good morning", "Selamat pagi");
        englishToMalayTranslations.put("Good night", "Selamat malam");
        englishToMalayTranslations.put("How are you?", "Apa khabar?");
        englishToMalayTranslations.put("Thank you", "Terima kasih");
        englishToMalayTranslations.put("Goodbye", "Selamat tinggal");
        englishToMalayTranslations.put("What's up?", "Ada apa?");

        Map<String, String> englishToArabicTranslations = new HashMap<>();
        englishToArabicTranslations.put("Good morning", "الخير صباح");
        englishToArabicTranslations.put("Good night", "مساؤك طاب");
        englishToArabicTranslations.put("How are you?", "حالك؟ كيف");
        englishToArabicTranslations.put("Thank you", "لك شكرا");
        englishToArabicTranslations.put("Goodbye", "السالمة مع");
        englishToArabicTranslations.put("What's up?", "أخبارك؟ ما");

        Map<String, String> englishToKoreanTranslations = new HashMap<>();
        englishToKoreanTranslations.put("Good morning", "좋은 아침");
        englishToKoreanTranslations.put("Good night", "안녕히 주무세요");
        englishToKoreanTranslations.put("How are you?", "어떻게 지내세요?");
        englishToKoreanTranslations.put("Thank you", "감사합니다");
        englishToKoreanTranslations.put("Goodbye", "안녕");
        englishToKoreanTranslations.put("What's up?", "뭐야?");

        translations.put("Bahasa Malaysia", englishToMalayTranslations);
        translations.put("Arabic", englishToArabicTranslations);
        translations.put("Korean", englishToKoreanTranslations);
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client connection...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String englishText = reader.readLine();
                String targetLanguage = reader.readLine();
                String translatedText = translateText(englishText, targetLanguage);
                writer.println(translatedText);

                reader.close();
                writer.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String translateText(String englishText, String targetLanguage) {
        Map<String, String> translationsForLanguage = translations.get(targetLanguage);
        if (translationsForLanguage != null) {
            String translatedText = translationsForLanguage.get(englishText);
            if (translatedText != null) {
                return translatedText;
            }
        }
        return "Translation not available.";
    }
}

