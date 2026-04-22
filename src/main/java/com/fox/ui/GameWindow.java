package com.fox.ui;

import com.fox.game.GameLogic;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String WIN_ICON_NAME = "win-icon.jpeg";
    private static final String LOSE_ICON_NAME = "lose-icon.jpeg";
    private static final String[] LANDSCAPE_NAMES = {
            "landscape-1.jpeg",
            "landscape-2.jpeg",
            "landscape-3.jpeg",
            "landscape-4.jpeg",
            "landscape-5.jpeg"
    };

    private JTextField inputField;
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JLabel landscapeLabel;

    private final transient GameLogic gameLogic = new GameLogic();
    private final Random random = new Random();

    private final Color bgColor = new Color(220, 255, 220);
    private final Color buttonColor = new Color(120, 200, 120);
    private final Color hoverColor = new Color(90, 180, 90);
    private final Color borderColor = new Color(80, 160, 80);

    private static final String COMPUTER_WON_MESSAGE = "Комп'ютер переміг!";
    private static final String FONT_NAME_ARIAL = "Arial";


    public GameWindow() {
        setTitle("Cities Game");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        updateScoreLabel();

        if (!gameLogic.hasCities()) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("Список міст не знайдено або він порожній");
            inputField.setEnabled(false);
        }

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(bgColor);

        landscapeLabel = createLandscapeLabel();
        mainPanel.add(landscapeLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        contentPanel.setBackground(bgColor);

        resultLabel = new JLabel("Введіть назву міста українською:", SwingConstants.CENTER);
        resultLabel.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 14));
        contentPanel.add(resultLabel);

        inputField = new JTextField();
        inputField.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 16));
        inputField.addActionListener(e -> handleMove());
        contentPanel.add(inputField);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 13));
        scoreLabel.setForeground(new Color(0, 90, 0));
        contentPanel.add(scoreLabel);

        JButton moveButton = new JButton("Зробити хід");
        moveButton.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 14));
        moveButton.addActionListener(e -> handleMove());

        JButton giveUpButton = new JButton("Здатись");
        giveUpButton.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 14));
        giveUpButton.addActionListener(e -> handleGiveUp());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBackground(bgColor);
        buttonsPanel.add(createButtonWrapper(moveButton));
        buttonsPanel.add(createButtonWrapper(giveUpButton));

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setBackground(bgColor);
        bottomPanel.add(contentPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLandscapeLabel() {
        JLabel label = new JLabel("Додайте landscape-1.jpeg ... landscape-5.jpeg", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(360, 250));
        label.setOpaque(true);
        label.setBackground(new Color(190, 235, 205));
        label.setForeground(new Color(0, 90, 0));
        label.setFont(new Font(FONT_NAME_ARIAL, Font.BOLD, 13));
        label.setBorder(BorderFactory.createLineBorder(borderColor, 2));

        updateLandscapeImage(label);

        return label;
    }

    private JPanel createButtonWrapper(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(buttonColor);
        buttonWrapper.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        buttonWrapper.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        button.addMouseListener(hover);
        buttonWrapper.add(button);

        return buttonWrapper;
    }

    private void handleMove() {
        String userInput = inputField.getText();

        String response = gameLogic.processMove(userInput);

        if (gameLogic.isGameOver()) {
            if (gameLogic.isUserGaveUp()) {
                finishGame(COMPUTER_WON_MESSAGE, gameLogic.getGameResult(), LOSE_ICON_NAME);
            } else {
                finishGame("Ви перемогли!", gameLogic.getGameResult(), WIN_ICON_NAME);
            }
            return;
        }

        if (isError(response)) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText(toHtml(response));
            updateScoreLabel();
            inputField.selectAll();
            inputField.requestFocusInWindow();
            return;
        } else {
            resultLabel.setForeground(new Color(0, 100, 0));
            resultLabel.setText(toHtml(
                    "Ваше місто: " + gameLogic.getLastUserCity()
                    + "<br>"
                    + response
            ));
        }

        updateLandscapeImage(landscapeLabel);
        updateScoreLabel();
        inputField.setText("");
    }

    private void handleGiveUp() {
        gameLogic.processMove("здаюсь");

        if (gameLogic.isGameOver()) {
            if (gameLogic.isUserGaveUp()) {
                finishGame(COMPUTER_WON_MESSAGE, gameLogic.getGameResult(), LOSE_ICON_NAME);
            } else {
                finishGame("Ви перемогли!", gameLogic.getGameResult(), WIN_ICON_NAME);
            }
        }
    }

    private boolean isError(String response) {
        return response.contains("відсутнє") ||
               response.contains("вже використано") ||
               response.contains("Має бути") ||
               response.contains("Введіть") ||
               response.contains("Список міст");
    }

    private String toHtml(String text) {
        return "<html>" + text + "</html>";
    }

    private void finishGame(String title, String message, String iconName) {
        int answer = showResultDialog(title, message, iconName);

        dispose();

        if (answer == JOptionPane.YES_OPTION) {
            new GameWindow();
        } else {
            System.exit(0);
        }
    }

    private int showResultDialog(String title, String message, String iconName) {
        Icon icon = loadResultIcon(iconName);
        String fullMessage = message + "\nРахунок: Ви "
                             + gameLogic.getUserScore()
                             + " : "
                             + gameLogic.getComputerScore()
                             + "\n\nГрати ще раз?";

        Object[] options = {"Так", "Ні"};

        if (icon != null) {
            return JOptionPane.showOptionDialog(
                    this,
                    fullMessage,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    icon,
                    options,
                    options[0]
            );
        }

        return JOptionPane.showOptionDialog(
                this,
                fullMessage,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    private Icon loadResultIcon(String iconName) {
        URL imageUrl = getClass().getClassLoader().getResource(iconName);

        if (imageUrl == null) {
            return null;
        }

        return new ImageIcon(imageUrl);
    }

    private Icon loadLandscapeIcon() {
        List<String> availableLandscapes = new ArrayList<>();

        for (String landscapeName : LANDSCAPE_NAMES) {
            if (getClass().getClassLoader().getResource(landscapeName) != null) {
                availableLandscapes.add(landscapeName);
            }
        }

        if (availableLandscapes.isEmpty()) {
            return null;
        }

        String landscapeName = availableLandscapes.get(random.nextInt(availableLandscapes.size()));
        URL imageUrl = getClass().getClassLoader().getResource(landscapeName);
        Image image = new ImageIcon(imageUrl).getImage();
        Image scaledImage = image.getScaledInstance(360, 250, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImage);
    }

    private void updateLandscapeImage(JLabel label) {
        Icon landscapeIcon = loadLandscapeIcon();

        if (landscapeIcon == null) {
            label.setText("Додайте landscape-1.jpeg ... landscape-5.jpeg");
            label.setIcon(null);
            return;
        }

        label.setText("");
        label.setIcon(landscapeIcon);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Рахунок: Ви "
                           + gameLogic.getUserScore()
                           + " : "
                           + gameLogic.getComputerScore()
                           + " Комп'ютер");
    }
}
