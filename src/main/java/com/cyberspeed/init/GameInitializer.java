package com.cyberspeed.init;

import com.cyberspeed.model.ScratchGameConfiguration;
import com.cyberspeed.engine.ScratchGameEngine;
import com.cyberspeed.ui.ScratchGamePanel;
import com.cyberspeed.util.LoggingUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The {@code GameInitializer} class is responsible for setting up the Scratch Game environment.
 * It initializes the game engine and game panel using the provided configuration and betting amount.
 * The class handles any exceptions that occur during initialization by logging the error and displaying
 * an error message to the user. If initialization fails, the application will exit.
 */
public class GameInitializer {

  private static final Logger LOGGER = LoggingUtils.getLogger(GameInitializer.class.getName());

  private ScratchGamePanel gamePanel;

  /**
   * Initializes the game with the specified configuration and betting amount.
   * Sets up the game engine and game panel, and logs the initialization process.
   *
   * @param config the game configuration
   * @param bettingAmount the betting amount for the game
   */
  public void initialize(ScratchGameConfiguration config, int bettingAmount) {
    LOGGER.info("Initializing game with configuration and betting amount: " + bettingAmount);
    try {
      ScratchGameEngine engine = new ScratchGameEngine(config, bettingAmount);
      LOGGER.info("Game engine initialized successfully.");
      this.gamePanel = new ScratchGamePanel(engine, bettingAmount);
      LOGGER.info("Game panel created successfully.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to initialize game", e);
      handleInitializationError(e);
    }
  }

  /**
   * Retrieves the initialized game panel.
   *
   * @return the game panel
   */
  public ScratchGamePanel getGamePanel() {
    return gamePanel;
  }

  /**
   * Handles errors that occur during game initialization.
   * Logs the error and displays an error message to the user, then exits the application.
   *
   * @param e the exception that occurred during initialization
   */
  private void handleInitializationError(Exception e) {
    JOptionPane.showMessageDialog(null,
        "Failed to initialize game: " + e.getMessage(),
        "Initialization Error",
        JOptionPane.ERROR_MESSAGE);
    System.exit(1);
  }
}
