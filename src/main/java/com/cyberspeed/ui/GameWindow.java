package com.cyberspeed.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The {@code GameWindow} class is responsible for creating and managing the main window
 * of the Scratch Game application. It provides methods to display the game panel within
 * a JFrame and to close the window when the game is finished. The window is initialized
 * with a specified title, width, and height, and is centered on the screen by default.
 */
public class GameWindow {

  private final JFrame frame;

  public GameWindow(String title, int width, int height) {
    frame = new JFrame(title);
    frame.setSize(width, height);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null); // Center the window on the screen
  }

  public void display(JPanel gamePanel) {
    frame.add(gamePanel);
    frame.setVisible(true); // Show the window
  }

  public void close() {
    frame.setVisible(false);
    frame.dispose();
  }
}
