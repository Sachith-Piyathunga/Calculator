package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * A modern, functional calculator application built with Java Swing.
 * Supports basic operations, percentage, square root, sign toggle, and chained expressions.
 */
public class Calculator {

    // Window dimensions
    int boardWidth = 360;
    int boardHeight = 540;

    // Custom theme colors
    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

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
    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    // Logic state variables
    String A = null;                       // First operand
    String operator = null;               // Operator (+, -, ×, ÷)
    boolean waitingForSecondOperand = false; // Flag to check if input is second number
    boolean justEvaluated = false;        // Prevents unwanted appending after '='

    /**
     * Constructor to set up the calculator interface and event logic.
     */
    public Calculator() {
        // Configure the main frame
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Configure display label
        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        // Add display to panel
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        // Configure button panel
        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        // Create and add buttons
        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            // Set button colors by type
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            // Attach event handler
            button.addActionListener(e -> {
                String value = button.getText();
                String currentDisplay = displayLabel.getText();

                // Number input
                if ("0123456789".contains(value)) {
                    if (currentDisplay.equals("0") || waitingForSecondOperand || justEvaluated) {
                        displayLabel.setText(value);
                        waitingForSecondOperand = false;
                        justEvaluated = false;
                    } else {
                        displayLabel.setText(currentDisplay + value);
                    }

                    // Decimal point
                } else if (value.equals(".")) {
                    if (justEvaluated || waitingForSecondOperand) {
                        displayLabel.setText("0.");
                        waitingForSecondOperand = false;
                        justEvaluated = false;
                    } else if (!currentDisplay.contains(".")) {
                        displayLabel.setText(currentDisplay + ".");
                    }

                    // Clear all
                } else if (value.equals("AC")) {
                    clearAll();
                    displayLabel.setText("0");

                    // Toggle sign
                } else if (value.equals("+/-")) {
                    double current = Double.parseDouble(currentDisplay);
                    current *= -1;
                    displayLabel.setText(removeZeroDecimal(current));

                    // Square root
                } else if (value.equals("√")) {
                    double current = Double.parseDouble(currentDisplay);
                    if (current >= 0) {
                        displayLabel.setText(removeZeroDecimal(Math.sqrt(current)));
                        justEvaluated = true;
                    }

                    // Percentage
                } else if (value.equals("%")) {
                    double current = Double.parseDouble(currentDisplay);
                    current /= 100;
                    displayLabel.setText(removeZeroDecimal(current));
                    justEvaluated = true;

                    // Equals sign
                } else if (value.equals("=")) {
                    if (A != null && operator != null && !waitingForSecondOperand) {
                        double numA = Double.parseDouble(A);
                        double numB = Double.parseDouble(currentDisplay);
                        double result = 0;

                        // Perform calculation
                        switch (operator) {
                            case "+" -> result = numA + numB;
                            case "-" -> result = numA - numB;
                            case "×" -> result = numA * numB;
                            case "÷" -> result = numB == 0 ? 0 : numA / numB;
                        }

                        displayLabel.setText(removeZeroDecimal(result));
                        clearAll();
                        justEvaluated = true;
                    }

                    // Operator buttons (+, -, ×, ÷)
                } else if (Arrays.asList(new String[]{"÷", "×", "-", "+"}).contains(value)) {
                    if (A != null && operator != null && !waitingForSecondOperand) {
                        // Chain evaluation if needed
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
                    } else {
                        A = currentDisplay;
                    }

                    operator = value;
                    waitingForSecondOperand = true;
                    justEvaluated = false;
                }
            });

            // Add button to panel
            buttonsPanel.add(button);
        }

        // Finalize and show the UI
        frame.setVisible(true);
    }

    /**
     * Resets calculator's internal state (but not the display).
     */
    void clearAll() {
        A = null;
        operator = null;
        waitingForSecondOperand = false;
        justEvaluated = false;
    }

    /**
     * Utility method to format numbers.
     * Removes ".0" from whole numbers for cleaner display.
     *
     * @param numDisplay The number to format
     * @return Formatted number as a string
     */
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}
