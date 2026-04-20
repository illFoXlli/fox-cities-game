package com.fox.ui;

import com.fox.game.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.net.URL;

public class GameWindow extends JFrame {

    private static final String WIN_ICON_NAME = "win-icon.jpeg";
    private static final String LOSE_ICON_NAME = "icon.jpeg";

    private JTextField inputField;
    private JLabel resultLabel;

    private final GameLogic gameLogic = new GameLogic();

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
            showResultDialog("Ви перемогли!", gameLogic.getGameResult(), WIN_ICON_NAME);

            dispose();
            new StartWindow();
            return;
        }

        if (isError(response)) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText(toHtml(response));
        } else {
            resultLabel.setForeground(new Color(0, 100, 0)); // зелёный
            resultLabel.setText(toHtml(
                    "Ваше місто: " + gameLogic.getLastUserCity()
                    + "<br>"
                    + response
            ));
        }

        inputField.setText("");
    }

    private void handleGiveUp() {
        showResultDialog("Комп'ютер переміг!", "Комп'ютер переміг!", LOSE_ICON_NAME);

        dispose();
        new StartWindow();
    }

    private boolean isError(String response) {
        return response.contains("відсутнє") ||
               response.contains("вже використано") ||
               response.contains("Має бути") ||
               response.contains("Введіть");
    }

    private String toHtml(String text) {
        return "<html>" + text + "</html>";
    }

    private void showResultDialog(String title, String message, String iconName) {
        Icon icon = loadResultIcon(iconName);
        String fullMessage = message + "\nРахунок: Ви "
                             + gameLogic.getUserScore()
                             + " : "
                             + gameLogic.getComputerScore();

        if (icon != null) {
            JOptionPane.showMessageDialog(
                    this,
                    fullMessage,
                    title,
                    JOptionPane.INFORMATION_MESSAGE,
                    icon
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                fullMessage,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private Icon loadResultIcon(String iconName) {
        URL imageUrl = getClass().getClassLoader().getResource(iconName);

        if (imageUrl == null) {
            return null;
        }

        return new ImageIcon(imageUrl);
    }
}
