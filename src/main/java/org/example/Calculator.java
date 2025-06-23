package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * A fully functional GUI calculator built using Java Swing.
 * Supports basic arithmetic operations, percentage, sign toggle, and square root.
 * Displays results with formatting and responsive button layout.
 */
public class Calculator {

    // Set window size
    int boardWidth = 360;
    int boardHeight = 540;

    // Set custom color theme for buttons and UI

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    // Add button layout and symbol groups
    String[] buttonValues = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    // Set UI Components
    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    // Set variables to store operands and operator
    String A = "0";
    String operator = null;
    String B = null;

    /**
     * Constructor that initializes the calculator UI and logic.
     */
    public Calculator() {
        // Set up main frame
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Configure display label (screen)
        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        // Setup display panel
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);

        // Set up buttons panel
        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customBlack);
        frame.add(buttonsPanel);

        // Create and configure each button
        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            // Color styling for symbol group
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            }else {
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }
            buttonsPanel.add(button);

            // Add action listener to button
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();
                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue == "=") {
                            if (A != null) {
                                B = displayLabel.getText();
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);

                                // Perform selected operation
                                if (operator == "+") {
                                    displayLabel.setText(removeZeroDecimal(numA+numB));
                                }
                                else if (operator == "-") {
                                    displayLabel.setText(removeZeroDecimal(numA-numB));
                                }
                                else if (operator == "×") {
                                    displayLabel.setText(removeZeroDecimal(numA*numB));
                                }
                                else if (operator == "÷") {
                                    displayLabel.setText(removeZeroDecimal(numA/numB));
                                }
                                clearAll(); // Reset after evaluation
                            }

                            // Store operand and operator
                            else if ("+-×÷".contains(buttonValue)) {
                                if (operator == null) {
                                    A = displayLabel.getText();
                                    displayLabel.setText("0");
                                    B = "0";
                                }
                                operator = buttonValue;
                            }
                        }

                        // Handle special symbols (AC, %, +/-)
                        else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                            if (buttonValue == "AC") {
                                clearAll();
                                displayLabel.setText("0");
                            }
                            else if (buttonValue == "+/-") {
                                double numDisplay = Double.parseDouble(displayLabel.getText());
                                numDisplay *= -1;
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            }
                            else if (buttonValue == "%") {
                                double numDisplay = Double.parseDouble(displayLabel.getText());
                                numDisplay /= 100;
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            }
                        }

                        // Handle numbers and dot
                        else {
                            if (buttonValue.equals("=")) {
                                if (!displayLabel.getText().contains(buttonValue)) {
                                    displayLabel.setText(displayLabel.getText() + buttonValue);
                                }
                            }
                            else if ("0123456789".contains(buttonValue)) {
                                if (displayLabel.getText() == "0") {
                                    displayLabel.setText(buttonValue);
                                }
                                else {
                                    displayLabel.setText(displayLabel.getText() + buttonValue);
                                }
                            }
                        }
                    }
                });
            }
                // Make frame visible after all components are added
                frame.setVisible(true);
        }
    }

        /**
         * Resets calculator state after calculation or AC.
         */
        void clearAll() {
            A = "0";
            operator = null;
            B = null;
        }

        /**
         * Utility method to clean up decimal display:
         * Removes trailing .0 if number is whole (e.g., 4.0 ➝ 4)
         */
        String removeZeroDecimal(double numDisplay) {
            if (numDisplay % 1 == 0) {
                return Integer.toString((int) numDisplay);
            }
            return Double.toString(numDisplay);
        }


}