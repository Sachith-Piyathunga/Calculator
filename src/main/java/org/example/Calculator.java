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
}
