package com.fox;

import com.fox.ui.StartWindow;
import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindow::new);
    }
}