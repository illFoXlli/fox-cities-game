package com.fox.ui;

import com.fox.game.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class GameWindow extends JFrame {

    private JTextField inputField;
    private JLabel resultLabel;

    private GameLogic gameLogic = new GameLogic();

    // 🎨 цвета (единый стиль)
    Color bgColor = new Color(220, 255, 220);
    Color buttonColor = new Color(120, 200, 120);
    Color hoverColor = new Color(90, 180, 90);
    Color borderColor = new Color(80, 160, 80);

    public GameWindow() {
        setTitle("Cities Game");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        // 🧱 основная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(bgColor);

        // 🏷 текст (исправленный + красивее)
        resultLabel = new JLabel("Введіть назву міста українською:", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(resultLabel, BorderLayout.NORTH);

        // ✏️ поле ввода (больше и жирнее)
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.BOLD, 16));
        inputField.addActionListener(e -> handleMove());
        mainPanel.add(inputField, BorderLayout.CENTER);

        // 🔘 кнопка "Здатись"
        JButton giveUpButton = new JButton("Здатись");
        giveUpButton.setFont(new Font("Arial", Font.BOLD, 14));

        giveUpButton.addActionListener(e -> handleGiveUp());

        // отключаем дефолт стиль
        giveUpButton.setFocusPainted(false);
        giveUpButton.setBorderPainted(false);
        giveUpButton.setContentAreaFilled(false);
        giveUpButton.setOpaque(false);

        // 🟢 wrapper (фон + скругление)
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(buttonColor);
        buttonWrapper.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));

        // курсор
        buttonWrapper.setCursor(new Cursor(Cursor.HAND_CURSOR));
        giveUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover (ВАЖНО — на оба элемента)
        MouseAdapter hover = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                buttonWrapper.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                buttonWrapper.setBackground(buttonColor);
            }
        };

        buttonWrapper.addMouseListener(hover);
        giveUpButton.addMouseListener(hover);

        buttonWrapper.add(giveUpButton);

        mainPanel.add(buttonWrapper, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleMove() {
        String userInput = inputField.getText();

        String response = gameLogic.processMove(userInput);

        if (gameLogic.isGameOver()) {
            JOptionPane.showMessageDialog(
                    this,
                    gameLogic.getGameResult() + "\nРахунок: Ви "
                    + gameLogic.getUserScore()
                    + " : "
                    + gameLogic.getComputerScore()
            );

            dispose();
            new StartWindow();
            return;
        }

        // если ошибка — красный
        if (response.contains("відсутнє") ||
            response.contains("вже використано") ||
            response.contains("Має бути")) {

            resultLabel.setForeground(Color.RED);
        } else {
            resultLabel.setForeground(new Color(0, 100, 0)); // зелёный
        }

// текст с контекстом
        resultLabel.setText("<html>" +
                            "Ваше місто: " + gameLogic.getLastUserCity() + "<br>" +
                            "Комп'ютер: " + gameLogic.getLastComputerCity() +
                            "</html>");
        inputField.setText("");
    }

    private void handleGiveUp() {
        ImageIcon icon = new ImageIcon("src/main/resources/icon.jpeg");

        JOptionPane.showMessageDialog(
                this,
                "Комп'ютер переміг!",
                "Результат",
                JOptionPane.INFORMATION_MESSAGE,
                icon
        );

        dispose();
        new StartWindow();
    }
}