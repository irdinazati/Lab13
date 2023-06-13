package Exercise7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class TranslationClientGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField englishTextField;
    private JComboBox<String> languageComboBox;
    private JTextArea translationTextArea;

    public TranslationClientGUI() {
        setTitle("Translation Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel englishLabel = new JLabel("English Text:");
        englishTextField = new JTextField();
        JLabel languageLabel = new JLabel("Target Language:");
        languageComboBox = new JComboBox<>();
        languageComboBox.addItem("English");
        languageComboBox.addItem("Bahasa Malaysia");
        languageComboBox.addItem("Arabic");
        languageComboBox.addItem("Korean");
        JButton translateButton = new JButton("Translate");
        translateButton.addActionListener(new TranslateButtonListener());
        JLabel translationLabel = new JLabel("Translation:");
        translationTextArea = new JTextArea();
        translationTextArea.setEditable(false);

        panel.add(englishLabel);
        panel.add(englishTextField);
        panel.add(languageLabel);
        panel.add(languageComboBox);
        panel.add(translateButton);
        panel.add(translationLabel);

        add(panel, BorderLayout.CENTER);
        add(translationTextArea, BorderLayout.SOUTH);
    }

    private class TranslateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String englishText = englishTextField.getText();
            String targetLanguage = (String) languageComboBox.getSelectedItem();

            try {
                Socket socket = new Socket("localhost", 12345);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                writer.println(englishText);
                writer.println(targetLanguage);

                String translatedText = reader.readLine();

                translationTextArea.setText(translatedText);

                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TranslationClientGUI clientGUI = new TranslationClientGUI();
            clientGUI.setVisible(true);
        });
    }
}
