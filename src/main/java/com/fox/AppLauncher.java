package com.fox;
import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cities Game");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JButton button = new JButton("Start");
        frame.add(button);

        frame.setVisible(true);
    }
}
