package com.cyberspeed;

import com.cyberspeed.cli.CliArgsParser;
import com.cyberspeed.config.ConfigParser;
import com.cyberspeed.exception.CliArgumentException;
import com.cyberspeed.init.GameInitializer;
import com.cyberspeed.model.ScratchGameConfiguration;
import com.cyberspeed.ui.GameWindow;
import com.cyberspeed.ui.ScratchGamePanel;
import com.cyberspeed.util.LoggingUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The {@code ScratchGameLauncher} class serves as the entry point for the Scratch Game application.
 * It is responsible for parsing command-line arguments, loading the game configuration, initializing
 * the game components, and launching the game window. The class handles exceptions that may occur
 * during these processes, logging errors and displaying error messages to the user. It ensures that
 * the game is started with the correct configuration and betting amount, and manages the lifecycle
 * of the game window.
 */
public class ScratchGameLauncher {

  private static final Logger LOGGER = LoggingUtils.getLogger(ScratchGameLauncher.class.getName());

  private final ConfigParser configParser;
  private final GameInitializer initializer;
  private final GameWindow window;
  private final int bettingAmount;

  public ScratchGameLauncher(String configPath, int bettingAmount) {
    this.configParser = new ConfigParser(configPath);
    this.bettingAmount = bettingAmount;
    this.initializer = new GameInitializer();
    this.window = new GameWindow("Scratch Game", 600, 600);

    LOGGER.info("Initialized ScratchGameLauncher with config: " + configPath + " and bet: " + bettingAmount);
  }

  public void run() {
    try {
      LOGGER.info("Loading configuration...");
      final ScratchGameConfiguration config = configParser.load();

      LOGGER.info("Initializing game...");
      initializer.initialize(config, bettingAmount);

      final ScratchGamePanel panel = initializer.getGamePanel();
      LOGGER.info("Displaying game panel...");
      window.display(panel);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Game failed to start", e);
      JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Game Error", JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    try {
      LOGGER.info("Parsing CLI arguments...");
      var parser = new CliArgsParser();
      var commandLineArgs = parser.parse(args);

      String configPath = commandLineArgs.getRequired("config");
      int bettingAmount = Integer.parseInt(commandLineArgs.getRequired("betting-amount"));

      LOGGER.info("Launching game with config: " + configPath + " and bettingAmount: " + bettingAmount);
      SwingUtilities.invokeLater(() -> new ScratchGameLauncher(configPath, bettingAmount).run());

    } catch (CliArgumentException e) {
      LOGGER.warning("CLI Error: " + e.getMessage());
      System.exit(1);
    } catch (NumberFormatException e) {
      LOGGER.warning("Invalid betting amount input");
      System.exit(1);
    } catch (IllegalArgumentException e) {
      LOGGER.warning("Validation Error: " + e.getMessage());
      System.exit(1);
    }
  }
}