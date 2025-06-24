# 🧮 Java Calculator

 - A modern, feature-rich desktop calculator built using Java. This application supports a full range of standard operations, a dynamic UI that adapts to light/dark themes, and an interactive calculation history panel. Developed with responsiveness, usability, and maintainability in mind.

---

## 📌 Project Overview

- The **Calculator** provides a robust alternative to simple calculator apps. With built-in keyboard shortcuts, real-time UI updates, and a polished design, it's ideal for both casual use and demonstrating GUI design and event-driven programming skills in Java.

---

## ✨ Features

| Category | Description |
|----------|-------------|
| 🧮 Basic Operations | Addition `+`, Subtraction `-`, Multiplication `×`, Division `÷` |
| 🧠 Advanced Functions | Percentage `%`, Square Root `√`, Toggle Sign `+/-`, Decimal Point `.` |
| 💡 UI Themes | Toggle between **Light** and **Dark** mode via button or `F1` key |
| ⌨️ Keyboard Support | Perform calculations using keyboard shortcuts |
| 🕒 Calculation History | View your last 20 calculations in a scrollable side panel |
| 🧹 Clear Controls | Clear all input with `AC` or `ESC`, clear history with one button |
| 🔙 Backspace | Use `Backspace` key to remove the last digit |
| 🪄 Auto Formatting | Removes unnecessary `.0` (e.g., `5.0` ➜ `5`) for clean display |

---

## 🖼️ User Interface Structure

The UI is divided into two main sections:

- **Left Panel:**  
  Contains the display, keypad (with 20 buttons), and theme toggle.
  
- **Right Panel:**  
  Scrollable history log with a clear button, styled separately for easy visibility.

> Responsive layout using `BorderLayout` and `GridLayout` ensures consistent alignment across different screen sizes.

---

## 📁 Project Structure

```
src/
└── org.example/
├── Main.java # Entry point to launch the calculator
└── Calculator.java # Contains all logic, layout, and event handling
```

---

## 🧠 Calculator Logic

- **State Tracking Variables**
  - `A`: First operand
  - `operator`: Selected operation
  - `waitingForSecondOperand`: Ensures the second input isn't concatenated incorrectly
  - `justEvaluated`: Prevents repeated equals and manages history input
  - `currentExpression`: Full math expression string for history

- **Execution Flow**
  - Button press → input handler → display update → optional history logging
  - Theme toggle applies new color scheme using the `updateTheme()` method
  - Keyboard adapter maps key presses to equivalent button logic

---

## 🎨 Theme Design

| Light Mode | Dark Mode |
|------------|-----------|
| Bright background, black text | Dark background, white text |
| Soft light buttons | Muted dark buttons |
| Green accent for operators | Consistent in both themes |
| 🌓 Theme toggle icon | 🌙 (light) ↔ ☀️ (dark) |

> The theme switch is immediate and updates **all** UI components, including panels, buttons, and display.

---
