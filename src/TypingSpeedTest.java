import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TypingSpeedTest extends JFrame {
    private JTextPane textPane;
    private JTextArea typingArea;
    private JButton restartButton;
    private JPanel wpmPanel, accuracyPanel, timePanel;
    private JLabel wpmValueLabel, accuracyValueLabel, timeValueLabel, statusLabel;
    private JPanel topPanel, bottomPanel, statsPanel;
    private ArrayList<String> textSentences;
    private String currentText;
    private long startTime;
    private boolean isTestRunning = false;
    private javax.swing.Timer uiTimer;

    //color scheme
    private final Color BACKGROUND = new Color(26, 26, 26);
    private final Color CARD_BACKGROUND = new Color(40, 40, 40);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color ACCENT_COLOR = new Color(255, 186, 8);
    private final Color CORRECT_COLOR = new Color(133, 255, 144);
    private final Color INCORRECT_COLOR = new Color(255, 107, 107);
    private final Color UNTYPED_COLOR = new Color(136, 136, 136);

    public TypingSpeedTest() {
        setTitle("Speed Type - Test Your Typing Skills");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(BACKGROUND);
        loadTexts();
        initializeComponents();
        setupLayout();
        setupEventListeners();

        selectRandomText();
        typingArea.setEnabled(true);
        updateTextHighlight();
    }

    private void loadTexts() {
        textSentences = new ArrayList<>();
        String[] sentences = {
                "The quick brown fox jumps over the lazy dog near the riverbank",
                "Programming is the art of solving problems with elegant code solutions",
                "Practice makes perfect when learning new skills and developing expertise",
                "Technology has revolutionized the way we communicate and share information globally",
                "Music is a universal language that connects people across cultures and generations",
                "The mountain climber reached the summit after hours of challenging ascent",
                "Artificial intelligence is transforming industries and creating new possibilities",
                "Ocean waves crash against the rocky shore under the moonlit sky",
                "Education is the foundation for personal growth and societal development",
                "Time management skills are essential for achieving success in any field",
                "The library contains thousands of books filled with knowledge and stories",
                "Creative thinking leads to innovative solutions for complex problems",
                "Healthy habits include regular exercise, balanced nutrition, and adequate sleep",
                "The scientist discovered a new species in the depths of the rainforest",
                "Collaboration between team members results in more effective project outcomes"
        };
        Collections.addAll(textSentences, sentences);
    }

    private void initializeComponents() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(BACKGROUND);
        JLabel titleLabel = new JLabel("Speed Type Test");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        topPanel.add(titleLabel);

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setBackground(CARD_BACKGROUND);
        textPane.setForeground(TEXT_COLOR);
        textPane.setFont(new Font("JetBrains Mono", Font.PLAIN, 24));
        textPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        textPane.setCaretColor(ACCENT_COLOR);

        typingArea = new JTextArea(3, 50);
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        typingArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 24));
        typingArea.setEnabled(false);
        typingArea.setBackground(CARD_BACKGROUND);
        typingArea.setForeground(TEXT_COLOR);
        typingArea.setCaretColor(ACCENT_COLOR);
        typingArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        typingArea.setSelectionColor(ACCENT_COLOR);

        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BACKGROUND);

        wpmPanel = createStatsLabel("WPM", "0");
        accuracyPanel = createStatsLabel("Accuracy", "100%");
        timePanel = createStatsLabel("Time", "0s");

        statsPanel.add(wpmPanel);
        statsPanel.add(accuracyPanel);
        statsPanel.add(timePanel);

        bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(BACKGROUND);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND);

        restartButton = createStyledButton("Restart", ACCENT_COLOR);
        buttonPanel.add(restartButton);

        statusLabel = new JLabel("Start typing to begin the test!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setForeground(TEXT_COLOR);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createStatsLabel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(UNTYPED_COLOR);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(ACCENT_COLOR);

        if (title.equals("WPM")) {
            wpmValueLabel = valueLabel;
        } else if (title.equals("Accuracy")) {
            accuracyValueLabel = valueLabel;
        } else if (title.equals("Time")) {
            timeValueLabel = valueLabel;
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupLayout() {
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        centerPanel.add(textPane, BorderLayout.NORTH);
        centerPanel.add(typingArea, BorderLayout.CENTER);
        centerPanel.add(statsPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        restartButton.addActionListener(e -> resetTest());

        typingArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (!isTestRunning && !typingArea.getText().isEmpty()) {
                    startTest();
                }
                if (isTestRunning) {
                    updateTextHighlight();
                    updateStats();
                }
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (isTestRunning) {
                    updateTextHighlight();
                    updateStats();
                }
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        typingArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (isTestRunning && typingArea.getText().length() >= currentText.length()) {
                    endTest();
                }
            }
        });
    }

    private void selectRandomText() {
        Random random = new Random();
        currentText = textSentences.get(random.nextInt(textSentences.size()));
        textPane.setText(currentText);
    }

    private void startTest() {
        startTime = System.currentTimeMillis();
        isTestRunning = true;
        statusLabel.setText("Type the text above as quickly and accurately as possible!");

        updateTextHighlight();

        uiTimer = new javax.swing.Timer(100, e -> updateTimeDisplay());
        uiTimer.start();
    }

    private void updateTimeDisplay() {
        if (isTestRunning) {
            long elapsed = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsed / 1000);
            timeValueLabel.setText(seconds + "s");
        }
    }

    private void updateTextHighlight() {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.setCharacterAttributes(0, doc.getLength(),
                    StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY,
                            StyleConstants.Foreground, UNTYPED_COLOR), true);

            String typed = typingArea.getText();

            for (int i = 0; i < Math.min(typed.length(), currentText.length()); i++) {
                Color color = (typed.charAt(i) == currentText.charAt(i)) ? CORRECT_COLOR : INCORRECT_COLOR;
                doc.setCharacterAttributes(i, 1,
                        StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY,
                                StyleConstants.Foreground, color), true);
            }

            if (typed.length() < currentText.length()) {
                SimpleAttributeSet cursorStyle = new SimpleAttributeSet();
                StyleConstants.setBackground(cursorStyle, ACCENT_COLOR);
                StyleConstants.setForeground(cursorStyle, BACKGROUND);
                doc.setCharacterAttributes(typed.length(), 1, cursorStyle, true);
            }
        } catch (Exception e) {
        }
    }

    private void updateStats() {
        String typed = typingArea.getText();

        long elapsed = System.currentTimeMillis() - startTime;
        double minutes = elapsed / 60000.0;
        int words = typed.trim().isEmpty() ? 0 : typed.trim().split("\\s+").length;
        int wpm = minutes > 0 ? (int) (words / minutes) : 0;

        int correctChars = 0;
        for (int i = 0; i < Math.min(currentText.length(), typed.length()); i++) {
            if (currentText.charAt(i) == typed.charAt(i)) {
                correctChars++;
            }
        }

        double accuracy = typed.length() > 0 ? ((double) correctChars / typed.length()) * 100 : 100;

        wpmValueLabel.setText(String.valueOf(wpm));
        accuracyValueLabel.setText(String.format("%.0f%%", accuracy));
    }

    private void endTest() {
        isTestRunning = false;

        if (uiTimer != null) {
            uiTimer.stop();
        }

        updateStats();
        statusLabel.setText("Test completed! Click 'Restart' or start typing again.");

        typingArea.setEnabled(false);
    }

    private void resetTest() {
        isTestRunning = false;
        if (uiTimer != null) {
            uiTimer.stop();
        }

        typingArea.setText("");
        typingArea.setEnabled(true);
        selectRandomText();

        wpmValueLabel.setText("0");
        accuracyValueLabel.setText("100%");
        timeValueLabel.setText("0s");

        statusLabel.setText("Ready for a new test! Start typing to begin.");

        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.setCharacterAttributes(0, doc.getLength(),
                    StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY,
                            StyleConstants.Foreground, TEXT_COLOR), true);
        } catch (Exception e) {
            // Safe ignore
        }

        updateTextHighlight();

        typingArea.requestFocusInWindow();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingSpeedTest().setVisible(true));
    }
}
