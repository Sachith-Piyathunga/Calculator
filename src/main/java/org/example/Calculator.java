package org.example;

// Importing required classes for GUI and functionality
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

/**
 * Enhanced calculator with support for:
 * - Basic arithmetic operations
 * - History of calculations
 * - Light/Dark theme toggle
 * - Keyboard input
 */
public class Calculator {

    // Window dimensions
    int boardWidth = 600; // Increased width for history panel
    int boardHeight = 540;

    // Theme colors - Light mode
    Color lightGray = new Color(127, 190, 127);
    Color darkGray = new Color(81, 99, 129);
    Color black = new Color(73, 73, 73);
    Color orange = new Color(106, 206, 47);
    Color white = Color.WHITE;

    // Theme colors - Dark mode
    Color darkBg = new Color(37, 40, 47);
    Color darkButton = new Color(60, 60, 60);
    Color darkText = Color.WHITE;
    Color lightBg = new Color(245, 245, 245);
    Color lightButton = new Color(230, 230, 230);
    Color lightText = Color.BLACK;

    // Current theme state
    boolean isDarkMode = true; // Initial theme state

    // Button values in layout order
    String[] buttonValues = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };

    // Symbol groups for styling and logic
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    // UI components
    JFrame frame = new JFrame("Enhanced Calculator");
    JLabel displayLabel = new JLabel(); // Display for current input/result
    JPanel mainPanel = new JPanel(); // Container for left and right sections
    JPanel leftPanel = new JPanel(); // Calculator UI panel
    JPanel rightPanel = new JPanel(); // History panel
    JPanel displayPanel = new JPanel(); // Contains display label
    JPanel buttonsPanel = new JPanel(); // Button grid
    JPanel controlPanel = new JPanel(); // For theme toggle
    JButton themeToggle = new JButton("🌙"); // Toggle theme
    JButton clearHistoryBtn = new JButton("Clear History"); // Clear history button

    // History components
    JTextArea historyArea = new JTextArea(); // Shows previous calculations
    JScrollPane historyScroll; // Scroll container for history
    ArrayList<String> calculationHistory = new ArrayList<>(); // Holds history strings


    // Calculator logic state variables
    String A = null; // First operand
    String operator = null; // Current operator
    boolean waitingForSecondOperand = false; // Tracks if we're expecting the second operand
    boolean justEvaluated = false; // True if last input was '='
    String currentExpression = ""; // Expression string to log into history

    // Constructor: Builds the UI and sets up behavior
    public Calculator() {
        setupUI();      // Layout and component setup
        setupKeyboardListener();  // Enable keyboard support
        updateTheme(); // Apply initial theme
        frame.setVisible(true); // Show the window
    }

    // Layout and GUI configuration
    private void setupUI() {
        // Configure main frame
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main panel to hold calculator and history
        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel);

        // Left panel for calculator
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(360, boardHeight));

        // Right panel for history
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(240, boardHeight));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Control panel for theme toggle
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        themeToggle.setPreferredSize(new Dimension(50, 30));
        themeToggle.setFocusable(false);
        themeToggle.addActionListener(e -> toggleTheme());
        controlPanel.add(themeToggle);

        // Display setup
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 60)); // Slightly smaller for fit
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel, BorderLayout.CENTER);

        // Buttons setup
        buttonsPanel.setLayout(new GridLayout(5, 4, 2, 2));
        setupButtons(); // Create buttons and add action listeners

        // History setup
        setupHistoryPanel(); // Configure right side history panel

        // Add components to panels
        leftPanel.add(controlPanel, BorderLayout.NORTH);
        leftPanel.add(displayPanel, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
    }

    // Creates calculator buttons and attaches event handlers
    private void setupButtons() {
        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.setFocusable(false);
            button.setPreferredSize(new Dimension(80, 80));

            button.addActionListener(e -> handleButtonClick(buttonValue));
            buttonsPanel.add(button);
        }
    }

    // Sets up history panel on the right
    private void setupHistoryPanel() {
        JLabel historyTitle = new JLabel("History");
        historyTitle.setFont(new Font("Arial", Font.BOLD, 16));
        historyTitle.setHorizontalAlignment(JLabel.CENTER);

        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setMargin(new Insets(10, 10, 10, 10));

        historyScroll = new JScrollPane(historyArea);
        historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        clearHistoryBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        clearHistoryBtn.setFocusable(false);
        clearHistoryBtn.addActionListener(e -> clearHistory());

        rightPanel.add(historyTitle, BorderLayout.NORTH);
        rightPanel.add(historyScroll, BorderLayout.CENTER);
        rightPanel.add(clearHistoryBtn, BorderLayout.SOUTH);
    }

    // Keyboard event handling (maps keys to calculator buttons)
    private void setupKeyboardListener() {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                int keyCode = e.getKeyCode();

                // Number keys
                if (key >= '0' && key <= '9') {
                    handleButtonClick(String.valueOf(key));
                }
                // Operator keys
                else if (key == '+') {
                    handleButtonClick("+");
                } else if (key == '-') {
                    handleButtonClick("-");
                } else if (key == '*') {
                    handleButtonClick("×");
                } else if (key == '/') {
                    handleButtonClick("÷");
                    e.consume(); // Prevent default behavior
                } else if (key == '=' || keyCode == KeyEvent.VK_ENTER) {
                    handleButtonClick("=");
                } else if (key == '.') {
                    handleButtonClick(".");
                } else if (key == '%') {
                    handleButtonClick("%");
                }
                // Special keys
                else if (keyCode == KeyEvent.VK_ESCAPE) {
                    handleButtonClick("AC");
                } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                } else if (keyCode == KeyEvent.VK_F1) {
                    toggleTheme();
                }
            }
        });
        frame.setFocusable(true); // Ensure frame receives key input
    }

    // Main logic handler for calculator input
    private void handleButtonClick(String value) {
        String currentDisplay = displayLabel.getText();

        // Handle number input
        if ("0123456789".contains(value)) {
            if (currentDisplay.equals("0") || waitingForSecondOperand || justEvaluated) {
                displayLabel.setText(value);
                waitingForSecondOperand = false;
                justEvaluated = false;
                if (justEvaluated) {
                    currentExpression = value;
                } else if (waitingForSecondOperand) {
                    currentExpression += value;
                }
            } else {
                displayLabel.setText(currentDisplay + value);
                currentExpression += value;
            }
        }
        // Decimal input
        else if (value.equals(".")) {
            if (justEvaluated || waitingForSecondOperand) {
                displayLabel.setText("0.");
                waitingForSecondOperand = false;
                justEvaluated = false;
                currentExpression += "0.";
            } else if (!currentDisplay.contains(".")) {
                displayLabel.setText(currentDisplay + ".");
                currentExpression += ".";
            }
        }
        // Clear all
        else if (value.equals("AC")) {
            clearAll();
            displayLabel.setText("0");
            currentExpression = "";
        }
        // Toggle sign
        else if (value.equals("+/-")) {
            double current = Double.parseDouble(currentDisplay);
            current *= -1;
            displayLabel.setText(removeZeroDecimal(current));
        }
        // Square root
        else if (value.equals("√")) {
            double current = Double.parseDouble(currentDisplay);
            if (current >= 0) {
                String result = removeZeroDecimal(Math.sqrt(current));
                displayLabel.setText(result);
                addToHistory("√" + currentDisplay + " = " + result);
                justEvaluated = true;
                currentExpression = "";
            }
        }
        // Percentage
        else if (value.equals("%")) {
            double current = Double.parseDouble(currentDisplay);
            current /= 100;
            String result = removeZeroDecimal(current);
            displayLabel.setText(result);
            addToHistory(currentDisplay + "% = " + result);
            justEvaluated = true;
            currentExpression = "";
        }
        // Equals sign
        else if (value.equals("=")) {
            if (A != null && operator != null && !waitingForSecondOperand) {
                double numA = Double.parseDouble(A);
                double numB = Double.parseDouble(currentDisplay);
                double result = 0;

                switch (operator) {
                    case "+" -> result = numA + numB;
                    case "-" -> result = numA - numB;
                    case "×" -> result = numA * numB;
                    case "÷" -> result = numB == 0 ? 0 : numA / numB;
                }

                String resultStr = removeZeroDecimal(result);
                displayLabel.setText(resultStr);
                addToHistory(currentExpression + " = " + resultStr);
                clearAll();
                justEvaluated = true;
                currentExpression = "";
            }
        }
        // Operator buttons handling
        else if (Arrays.asList(new String[]{"÷", "×", "-", "+"}).contains(value)) {
            if (A != null && operator != null && !waitingForSecondOperand) {
                // Chain evaluation
                double numA = Double.parseDouble(A);
                double numB = Double.parseDouble(currentDisplay);
                double result = 0;

                switch (operator) {
                    case "+" -> result = numA + numB;
                    case "-" -> result = numA - numB;
                    case "×" -> result = numA * numB;
                    case "÷" -> result = numB == 0 ? 0 : numA / numB;
                }

                displayLabel.setText(removeZeroDecimal(result));
                A = removeZeroDecimal(result);
                currentExpression += " " + value + " ";
            } else {
                A = currentDisplay;
                if (currentExpression.isEmpty()) {
                    currentExpression = currentDisplay + " " + value + " ";
                } else {
                    currentExpression += " " + value + " ";
                }
            }

            operator = value;
            waitingForSecondOperand = true;
            justEvaluated = false;
        }
    }

    // Handle backspace key: removes last character
    private void handleBackspace() {
        String currentDisplay = displayLabel.getText();
        if (currentDisplay.length() > 1 && !currentDisplay.equals("0")) {
            displayLabel.setText(currentDisplay.substring(0, currentDisplay.length() - 1));
        } else {
            displayLabel.setText("0");
        }
    }

    // Adds a string to history
    private void addToHistory(String calculation) {
        calculationHistory.add(calculation);
        updateHistoryDisplay();
    }

    // Updates the text area with latest history
    private void updateHistoryDisplay() {
        StringBuilder sb = new StringBuilder();
        int start = Math.max(0, calculationHistory.size() - 20); // Show last 20 calculations

        for (int i = start; i < calculationHistory.size(); i++) {
            sb.append(calculationHistory.get(i)).append("\n");
        }

        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    // Clear history list and display
    private void clearHistory() {
        calculationHistory.clear();
        historyArea.setText("");
    }

    // Toggles between dark/light mode
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        updateTheme();
    }

    // Applies current theme colors to all components
    private void updateTheme() {
        Color bgColor, buttonColor, textColor, displayBg, panelBg;

        if (isDarkMode) {
            bgColor = darkBg;
            buttonColor = darkButton;
            textColor = darkText;
            displayBg = black;
            panelBg = darkBg;
            themeToggle.setText("☀️");
        } else {
            bgColor = lightBg;
            buttonColor = lightButton;
            textColor = lightText;
            displayBg = white;
            panelBg = lightBg;
            themeToggle.setText("🌙");
        }

        // Update frame and panels
        frame.getContentPane().setBackground(bgColor);
        mainPanel.setBackground(bgColor);
        leftPanel.setBackground(bgColor);
        rightPanel.setBackground(bgColor);
        displayPanel.setBackground(bgColor);
        buttonsPanel.setBackground(bgColor);
        controlPanel.setBackground(bgColor);

        // Update display
        displayLabel.setBackground(displayBg);
        displayLabel.setForeground(textColor);

        // Update history components
        historyArea.setBackground(displayBg);
        historyArea.setForeground(textColor);
        historyScroll.getViewport().setBackground(displayBg);
        clearHistoryBtn.setBackground(buttonColor);
        clearHistoryBtn.setForeground(textColor);

        // Update all buttons
        Component[] components = buttonsPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String text = btn.getText();

                if (Arrays.asList(topSymbols).contains(text)) {
                    btn.setBackground(isDarkMode ? lightGray : lightButton);
                    btn.setForeground(isDarkMode ? black : lightText);
                } else if (Arrays.asList(rightSymbols).contains(text)) {
                    btn.setBackground(orange);
                    btn.setForeground(white);
                } else {
                    btn.setBackground(buttonColor);
                    btn.setForeground(textColor);
                }
                btn.setBorder(new LineBorder(bgColor, 1));
            }
        }

        // Update theme toggle button
        themeToggle.setBackground(buttonColor);
        themeToggle.setForeground(textColor);
        themeToggle.setBorder(new LineBorder(bgColor, 1));

        frame.repaint();
    }

    // Reset calculator satate
    void clearAll() {
        A = null;
        operator = null;
        waitingForSecondOperand = false;
        justEvaluated = false;
    }

    // Removes unnecessary decimal places
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}