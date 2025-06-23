package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {

    int boardWidth = 360;
    int boardHeight = 540;

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    String[] buttonValues = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String A = null;
    String operator = null;
    boolean waitingForSecondOperand = false;
    boolean justEvaluated = false;

    public Calculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

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

            button.addActionListener(e -> {
                String value = button.getText();
                String currentDisplay = displayLabel.getText();

                if ("0123456789".contains(value)) {
                    if (currentDisplay.equals("0") || waitingForSecondOperand || justEvaluated) {
                        displayLabel.setText(value);
                        waitingForSecondOperand = false;
                        justEvaluated = false;
                    } else {
                        displayLabel.setText(currentDisplay + value);
                    }
                } else if (value.equals(".")) {
                    if (justEvaluated || waitingForSecondOperand) {
                        displayLabel.setText("0.");
                        waitingForSecondOperand = false;
                        justEvaluated = false;
                    } else if (!currentDisplay.contains(".")) {
                        displayLabel.setText(currentDisplay + ".");
                    }
                } else if (value.equals("AC")) {
                    clearAll();
                    displayLabel.setText("0");
                } else if (value.equals("+/-")) {
                    double current = Double.parseDouble(currentDisplay);
                    current *= -1;
                    displayLabel.setText(removeZeroDecimal(current));
                } else if (value.equals("√")) {
                    double current = Double.parseDouble(currentDisplay);
                    if (current >= 0) {
                        displayLabel.setText(removeZeroDecimal(Math.sqrt(current)));
                        justEvaluated = true;
                    }
                } else if (value.equals("%")) {
                    double current = Double.parseDouble(currentDisplay);
                    current /= 100;
                    displayLabel.setText(removeZeroDecimal(current));
                    justEvaluated = true;
                } else if (value.equals("=")) {
                    // Handle equals button
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

                        displayLabel.setText(removeZeroDecimal(result));
                        clearAll(); // Reset all except current display
                        justEvaluated = true;
                    }
                } else if (Arrays.asList(new String[]{"÷", "×", "-", "+"}).contains(value)) {
                    // Handle operator buttons
                    if (A != null && operator != null && !waitingForSecondOperand) {
                        // Chain operations: calculate previous operation first
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

            buttonsPanel.add(button);
        }

        frame.setVisible(true);
    }

    void clearAll() {
        A = null;
        operator = null;
        waitingForSecondOperand = false;
        justEvaluated = false;
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}