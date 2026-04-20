package com.fox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class StartWindow extends JFrame {

    public StartWindow() {

        setTitle("Cities Game");
        setSize(400, 140);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🎨 цвета
        Color bgColor = new Color(220, 255, 220);
        Color buttonColor = new Color(120, 200, 120);
        Color hoverColor = new Color(90, 180, 90);
        Color borderColor = new Color(80, 160, 80);

        // 🏷 заголовок
        JLabel label = new JLabel("Welcome to Cities Game", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        // 🔘 кнопка
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));

        startButton.addActionListener(e -> {
            new GameWindow();
            dispose();
        });

        // Enter = кнопка
        getRootPane().setDefaultButton(startButton);

        // ❌ отключаем стандартную отрисовку кнопки
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(false);

        // 🟢 wrapper (он рисует кнопку)
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(buttonColor);
        buttonWrapper.setBorder(BorderFactory.createLineBorder(
                borderColor, 2, true
        ));

        // 🖱 курсор (ВАЖНО — на wrapper)
        buttonWrapper.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🌗 hover (на wrapper)
        MouseAdapter hoverEffect = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                buttonWrapper.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                buttonWrapper.setBackground(buttonColor);
            }
        };

        buttonWrapper.addMouseListener(hoverEffect);
        startButton.addMouseListener(hoverEffect);

        // добавляем кнопку внутрь wrapper
        buttonWrapper.add(startButton);

        // 🧱 основная панель
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(bgColor);

        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonWrapper, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);
    }
}