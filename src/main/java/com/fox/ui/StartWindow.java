package com.fox.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;

public class StartWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color bgColor = new Color(220, 255, 220);
    private final Color buttonColor = new Color(120, 200, 120);
    private final Color hoverColor = new Color(90, 180, 90);
    private final Color borderColor = new Color(80, 160, 80);

    public StartWindow() {
        setTitle("Cities Game");
        setSize(400, 140);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JLabel label = new JLabel("Вітаємо у грі \"Міста\"", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JButton startButton = new JButton("Почати");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(e -> {
            new GameWindow();
            dispose();
        });

        getRootPane().setDefaultButton(startButton);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(bgColor);

        panel.add(label, BorderLayout.CENTER);
        panel.add(createButtonWrapper(startButton), BorderLayout.SOUTH);

        add(panel);
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

        MouseAdapter hover = new MouseAdapter() {
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
}
