package com.cyberspeed.ui;

import com.cyberspeed.model.PlayResult;
import com.cyberspeed.engine.ScratchGameEngine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The {@code ScratchGamePanel} class is a custom JPanel that serves as the user interface
 * for the Scratch Game. It integrates with the {@code ScratchGameEngine} to display the game
 * grid, manage user interactions, and show the results of the game. The panel includes controls
 * for starting the game, displaying the current bet amount, and showing the reward. It also
 * provides a text area to display the output JSON representation of the game result. The class
 * handles the layout and styling of the UI components and updates the display based on the
 * game state.
 */
public class ScratchGamePanel extends JPanel {

  private ScratchGameEngine gameEngine;
  private JButton[][] buttons;
  private JTextField betAmountField;
  private JLabel rewardLabel;
  private JTextArea jsonOutputArea;
  private int initialBetAmount;

  public ScratchGamePanel() {}

  public ScratchGamePanel(ScratchGameEngine gameEngine, int bettingAmount) {
    Objects.requireNonNull(gameEngine, "Game engine must not be null");
    this.gameEngine = gameEngine;
    this.initialBetAmount = bettingAmount;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout(10, 10));
    setBackground(new Color(245, 245, 245)); // Light background
    createControlPanel();
    createGameGrid();
    createInfoPanel();
  }

  private void createControlPanel() {
    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    controlPanel.setBackground(new Color(245, 245, 245));

    betAmountField = new JTextField(10);
    betAmountField.setEditable(false);
    betAmountField.setFocusable(false);
    betAmountField.setBackground(new Color(255, 255, 255));
    betAmountField.setForeground(Color.BLACK);
    betAmountField.setText(String.valueOf(initialBetAmount));
    betAmountField.setFont(new Font("SansSerif", Font.PLAIN, 14));

    JButton startGameButton = new JButton("Start Game");
    startGameButton.setFocusPainted(false);
    startGameButton.setFont(new Font("SansSerif", Font.BOLD, 14));

    rewardLabel = new JLabel("Reward: 0");
    rewardLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

    controlPanel.add(new JLabel("Bet Amount:"));
    controlPanel.add(betAmountField);
    controlPanel.add(startGameButton);
    controlPanel.add(rewardLabel);

    add(controlPanel, BorderLayout.NORTH);

    startGameButton.addActionListener(e -> handleGameStart());
  }

  private void createGameGrid() {
    int rows = gameEngine.getRows();
    int cols = gameEngine.getColumns();

    JPanel gameGridPanel = new JPanel(new GridLayout(rows, cols, 3, 3));
    gameGridPanel.setBackground(new Color(192,192,192));
    buttons = new JButton[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        JButton btn = new JButton("");
        btn.setPreferredSize(new Dimension(100, 100));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setBackground(new Color(169,169,169)); // White background
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        buttons[i][j] = btn;
        gameGridPanel.add(btn);
      }
    }

    add(gameGridPanel, BorderLayout.CENTER);
  }

  private void createInfoPanel() {
    JPanel infoPanel = new JPanel(new BorderLayout());
    infoPanel.setBackground(new Color(245, 245, 245));

    jsonOutputArea = new JTextArea(10, 50);
    jsonOutputArea.setEditable(false);
    jsonOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane jsonScrollPane = new JScrollPane(jsonOutputArea);

    JPanel jsonPanel = new JPanel(new BorderLayout());
    jsonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    jsonPanel.setBackground(new Color(245, 245, 245));
    jsonPanel.add(new JLabel("Output JSON:"), BorderLayout.NORTH);
    jsonPanel.add(jsonScrollPane, BorderLayout.CENTER);

    // Exit button panel
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.setBackground(new Color(245, 245, 245));

    JButton exitButton = new JButton("Exit");
    exitButton.setFocusPainted(false);
    exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
    exitButton.addActionListener(e -> System.exit(0));

    bottomPanel.add(exitButton);

    infoPanel.add(jsonPanel, BorderLayout.CENTER);
    infoPanel.add(bottomPanel, BorderLayout.SOUTH);

    add(infoPanel, BorderLayout.SOUTH);
  }

  private void handleGameStart() {
    try {
      startGame();
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(null, "Please enter a valid bet amount.");
    }
  }

  private void startGame() {
    gameEngine.startGame();
    updateGameUI();
  }

  private String generateOutputJson() {
    PlayResult result = new PlayResult(
        gameEngine.getMatrix(),
        gameEngine.getReward(),
        gameEngine.getAppliedWinningCombinations(),
        gameEngine.getAppliedBonusSymbol()
    );
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper.writeValueAsString(result);
    } catch (JsonProcessingException e) {
      return "{ \"error\": \"Failed to generate JSON\" }";
    }
  }

  private void updateGameUI() {
    String[][] matrix = gameEngine.getMatrix();
    for (int i = 0; i < gameEngine.getRows(); i++) {
      for (int j = 0; j < gameEngine.getColumns(); j++) {
        buttons[i][j].setText(matrix[i][j]);
        buttons[i][j].setEnabled(true);
      }
    }
    rewardLabel.setText("Reward: " + gameEngine.getReward());
    jsonOutputArea.setText(generateOutputJson());
  }
}
