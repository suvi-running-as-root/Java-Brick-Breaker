package game;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.6);  // Adjust width based on screen
        int height = (int) (screenSize.height * 0.7); // Adjust height based on screen

        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel(width, height);

        frame.setTitle("Brick Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(gamePanel);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);

        gamePanel.requestFocusInWindow(); // Ensures the panel has focus for KeyListener
    }
}
