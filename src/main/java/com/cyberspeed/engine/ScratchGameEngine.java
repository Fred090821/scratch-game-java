package com.cyberspeed.engine;

import com.cyberspeed.config.GameConfigModelValidator;
import com.cyberspeed.exception.InvalidBettingAmountException;
import com.cyberspeed.model.BonusSymbolProbability;
import com.cyberspeed.model.CombinationData;
import com.cyberspeed.model.Probabilities;
import com.cyberspeed.model.ScratchGameConfiguration;
import com.cyberspeed.model.StandardSymbolProbability;
import com.cyberspeed.model.SymbolDefinition;
import com.cyberspeed.model.WinCombination;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * The {@code ScratchGameEngine} class is responsible for managing the core logic of the Scratch Game.
 * It initializes the game configuration, validates the setup, and handles the game mechanics such as
 * generating the game matrix, applying bonus symbols, and calculating rewards based on winning combinations.
 * The class provides methods to start the game, process symbol combinations, and apply bonus effects.
 * It also maintains the state of the game, including the matrix, reward, and applied winning combinations.
 */
public class ScratchGameEngine {

  private int columns;
  private int rows;
  private Map<String, SymbolDefinition> symbols;
  private Probabilities probabilities;
  @JsonProperty("win_combinations")
  private Map<String, WinCombination> winCombinations;
  private final int bettingAmount;

  /**
   * Inner class to track the steps involved in calculating the reward.
   */
  private static class CalculationSteps {

    // 1. Symbol frequencies (e.g., "A" appears 8 times)
    Map<String, Integer> symbolCounts;

    // 2. Raw combinations detected (e.g., "same_symbol_8_times")
    Map<String, CombinationData> combinationSymbols;

    // 3. Best multiplier per group (e.g., "same_symbols" group uses x10)
    Map<String, Double> groupMultipliers = new HashMap<>();

    // 4. Base reward before applying bonuses
    double baseReward;

    // 5. Bonus symbol and its impact (e.g., "10x" or "MISS")
    String bonusImpact;

    // 6. Final reward after applying bonuses
    double totalReward;
  }

  // Tracks calculation steps for logging/breakdowns
  private CalculationSteps calculationSteps;

  private int reward;
  private String[][] matrix;
  private Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
  private String appliedBonusSymbol;

  /**
   * Constructs a {@code ScratchGameEngine} with the specified game configuration and betting amount.
   * Validates the configuration and initializes the game fields.
   *
   * @param gameConfig the game configuration
   * @param bettingAmount the betting amount for the game
   * @throws InvalidBettingAmountException if the betting amount is less than or equal to zero
   */
  public ScratchGameEngine(ScratchGameConfiguration gameConfig, int bettingAmount) {
    validateConstructorArguments(gameConfig);
    initializeFields(gameConfig);

    // used by tests
    if (bettingAmount <= 0) {
      throw new InvalidBettingAmountException("Betting amount must be greater than 0");
    }
    this.bettingAmount = bettingAmount;
  }

  /**
   * Validates the constructor arguments to ensure the game configuration is not null
   * and meets the required specifications.
   *
   * @param gameConfig the game configuration to validate
   */
  private void validateConstructorArguments(ScratchGameConfiguration gameConfig) {
    Objects.requireNonNull(gameConfig, "Game configuration cannot be null");

    GameConfigModelValidator.validateGridDimensions(gameConfig);
    GameConfigModelValidator.validateGameComponents(gameConfig);
  }

  /**
   * Initializes the fields of the game engine using the provided game configuration.
   *
   * @param gameConfig the game configuration
   */
  private void initializeFields(ScratchGameConfiguration gameConfig) {
    this.columns = gameConfig.getColumns();
    this.rows = gameConfig.getRows();
    this.symbols = gameConfig.getSymbols();
    this.probabilities = gameConfig.getProbabilities();
    this.winCombinations = gameConfig.getWinCombinations();
  }

  /**
   * Starts the game by resetting the reward, clearing applied combinations,
   * generating the game matrix, applying bonus symbols, and calculating the reward.
   */
  public void startGame() {
    this.reward = 0;
    this.appliedWinningCombinations.clear();
    this.appliedBonusSymbol = null;
    this.matrix = generateMatrix();
    applyBonusSymbol();
    calculateReward();
  }

  /**
   * Generates the game matrix by filling it with symbols based on their probabilities.
   *
   * @return the generated game matrix
   */
  private String[][] generateMatrix() {
    String[][] grid = new String[rows][columns];
    Random rand = new Random();

    // Fill standard symbols
    for (StandardSymbolProbability prob : probabilities.getStandardSymbols()) {
      int col = prob.getColumn();
      int row = prob.getRow();
      if (col < columns && row < rows) {
        grid[row][col] = selectSymbol(prob.getSymbols(), rand);
      }
    }

    // Fill remaining cells with first probability config
    StandardSymbolProbability defaultProb = probabilities.getStandardSymbols().get(0);
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        if (grid[r][c] == null) {
          grid[r][c] = selectSymbol(defaultProb.getSymbols(), rand);
        }
      }
    }

    return grid;
  }

  /**
   * Selects a symbol based on its weight from the provided symbol weights map.
   *
   * @param symbolWeights the map of symbols and their weights
   * @param rand the random number generator
   * @return the selected symbol
   */
  private String selectSymbol(Map<String, Integer> symbolWeights, Random rand) {

    int totalWeight = symbolWeights.values().stream().mapToInt(Integer::intValue).sum();
    if (totalWeight == 0) {
      return "MISS";
    }

    int randomNumber = rand.nextInt(totalWeight);
    int cumulativeWeight = 0;
    for (Map.Entry<String, Integer> entry : symbolWeights.entrySet()) {
      cumulativeWeight += entry.getValue();
      if (randomNumber < cumulativeWeight) {
        return entry.getKey();
      }
    }
    return "MISS";
  }

  /**
   * Applies a bonus symbol to the game matrix at a random position.
   */
  private void applyBonusSymbol() {
    BonusSymbolProbability bonusProb = probabilities.getBonusSymbols();
    String bonusSymbol = selectSymbol(bonusProb.getSymbols(), new Random());

    Random rand = new Random();
    int row = rand.nextInt(rows);
    int col = rand.nextInt(columns);

    matrix[row][col] = bonusSymbol;
    appliedBonusSymbol = bonusSymbol;
  }

  /**
   * Calculates the reward based on the current game matrix and applied bonus symbols.
   */
  private void calculateReward() {
    calculationSteps = new CalculationSteps();

    // 1. Count symbols and initialize tracking
    calculationSteps.symbolCounts = countStandardSymbols();
    calculationSteps.combinationSymbols = new HashMap<>();

    // 2. Process combinations
    processSameSymbolCombination(calculationSteps.symbolCounts, calculationSteps.combinationSymbols);
    processLinearCombination(calculationSteps.combinationSymbols);

    // 3. Determine best combination per group
    Map<String, String> groupBest = determineBestCombinationPerGroup(calculationSteps.combinationSymbols);

    // 4. Collect multipliers per symbol
    Map<String, Double> symbolMultipliers = new HashMap<>();
    appliedWinningCombinations.clear();

    // For each group's best combination
    for (String comboName : groupBest.values()) {
      CombinationData comboData = calculationSteps.combinationSymbols.get(comboName);
      WinCombination wc = winCombinations.get(comboName);
      double multiplier = wc.getRewardMultiplier();

      // Apply multiplier to all qualified symbols in this combination
      for (String symbol : comboData.getSymbols()) {
        // Multiply multipliers for the same symbol across groups
        symbolMultipliers.merge(symbol, multiplier, (old, newVal) -> old * newVal);

        // Track applied combinations
        appliedWinningCombinations
            .computeIfAbsent(symbol, k -> new ArrayList<>())
            .add(comboName);
      }
    }

    // 5. Calculate base reward
    calculationSteps.baseReward = symbolMultipliers.entrySet().stream()
        .mapToDouble(e -> bettingAmount
            * symbols.get(e.getKey()).getRewardMultiplier() // Symbol multiplier
            * e.getValue()) // Combined group multipliers
        .sum();

    // 6. Apply bonus effect
    calculationSteps.bonusImpact = getAppliedBonusSymbol();
    calculationSteps.totalReward = applyBonusSymbolEffect(calculationSteps.baseReward);
    this.reward = (int) Math.round(calculationSteps.totalReward);
  }

  /**
   * Applies the effect of the bonus symbol on the total reward.
   *
   * @param totalReward the base reward before applying the bonus
   * @return the total reward after applying the bonus effect
   */
  private double applyBonusSymbolEffect(double totalReward) {
    if (appliedBonusSymbol != null && totalReward > 0) {
      SymbolDefinition bonus = symbols.get(appliedBonusSymbol);
      switch (bonus.getImpact()) {
        case "multiply_reward":
          totalReward *= bonus.getRewardMultiplier();
          break;
        case "extra_bonus":
          totalReward += bonus.getExtra();
          break;
        case "miss":
          // No action
          break;
      }
    }
    return totalReward;
  }

  /**
   * Counts the occurrences of standard symbols in the game matrix.
   *
   * @return a map of symbol counts
   */
  private Map<String, Integer> countStandardSymbols() {
    Map<String, Integer> symbolCounts = new HashMap<>();
    for (String[] row : matrix) {
      for (String symbol : row) {
        SymbolDefinition def = symbols.get(symbol);
        if (def != null && "standard".equals(def.getType())) {
          symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
        }
      }
    }
    return symbolCounts;
  }

  /**
   * Processes combinations of the same symbol and updates the combination symbols map.
   *
   * @param symbolCounts the map of symbol counts
   * @param combinationSymbols the map to update with detected combinations
   */
  private void processSameSymbolCombination(Map<String, Integer> symbolCounts,
      Map<String, CombinationData> combinationSymbols) {
    // 2. Process same_symbol combinations
    for (Map.Entry<String, WinCombination> entry : winCombinations.entrySet()) {
      String comboName = entry.getKey();
      WinCombination wc = entry.getValue();
      if ("same_symbols".equals(wc.getWhen())) {
        Set<String> qualifiedSymbols = new HashSet<>();
        for (Map.Entry<String, Integer> symbolEntry : symbolCounts.entrySet()) {
          if (symbolEntry.getValue() >= wc.getCount()) {
            qualifiedSymbols.add(symbolEntry.getKey());
          }
        }
        if (!qualifiedSymbols.isEmpty()) {
          combinationSymbols.put(comboName, new CombinationData(
              wc.getGroup(),
              qualifiedSymbols
          ));

        }
      }
    }
  }

  /**
   * Determines the best combination for each group based on the detected combinations.
   *
   * @param combinationSymbols the map of detected combinations
   * @return a map of the best combination for each group
   */
  private Map<String, String> determineBestCombinationPerGroup(Map<String, CombinationData> combinationSymbols) {
    Map<String, String> groupBestCombination = new HashMap<>();
    for (Map.Entry<String, CombinationData> entry : combinationSymbols.entrySet()) {
      String comboName = entry.getKey();
      CombinationData data = entry.getValue();
      String group = data.getGroup();
      WinCombination wc = winCombinations.get(comboName);

      if (!groupBestCombination.containsKey(group) ||
          wc.getCount() > winCombinations.get(groupBestCombination.get(group)).getCount()) {
        groupBestCombination.put(group, comboName);
      }
    }
    return groupBestCombination;
  }

  /**
   * Processes linear symbol combinations and updates the combination symbols map.
   *
   * @param combinationSymbols the map to update with detected combinations
   */
  private void processLinearCombination(Map<String, CombinationData> combinationSymbols) {
    for (Map.Entry<String, WinCombination> entry : winCombinations.entrySet()) {
      String comboName = entry.getKey();
      WinCombination wc = entry.getValue();

      if ("linear_symbols".equals(wc.getWhen())) {
        Set<String> qualifiedSymbols = new HashSet<>();
        for (List<String> area : wc.getCoveredAreas()) {
          String symbol = checkLinearCombination(area);
          if (symbol != null) {
            qualifiedSymbols.add(symbol);
          }
        }
        if (!qualifiedSymbols.isEmpty()) {
          combinationSymbols.put(comboName, new CombinationData(
              wc.getGroup(),
              qualifiedSymbols
          ));
        }
      }
    }
  }

  /**
   * Checks for a linear combination of symbols in the specified area.
   *
   * @param area the area to check for a linear combination
   * @return the symbol if a linear combination is found, null otherwise
   */
  private String checkLinearCombination(List<String> area) {
    String target = null;
    for (String cell : area) {
      String[] parts = cell.split(":");
      int row = Integer.parseInt(parts[0]);
      int col = Integer.parseInt(parts[1]);

      if (row >= rows || col >= columns) {
        return null;
      }

      String symbol = matrix[row][col];
      SymbolDefinition def = symbols.get(symbol);
      if (def == null || !def.getType().equals("standard")) {
        return null;
      }

      if (target == null) {
        target = symbol;
      } else if (!target.equals(symbol)) {
        return null;
      }
    }
    return target;
  }

  // Getters and setters for various fields

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public Map<String, SymbolDefinition> getSymbols() {
    return symbols;
  }

  public void setSymbols(Map<String, SymbolDefinition> symbols) {
    this.symbols = symbols;
  }

  public Probabilities getProbabilities() {
    return probabilities;
  }

  public void setProbabilities(Probabilities probabilities) {
    this.probabilities = probabilities;
  }

  public Map<String, WinCombination> getWinCombinations() {
    return winCombinations;
  }

  public void setWinCombinations(Map<String, WinCombination> winCombinations) {
    this.winCombinations = winCombinations;
  }

  public int getReward() {
    return reward;
  }

  public void setReward(int reward) {
    this.reward = reward;
  }

  public String[][] getMatrix() {
    return matrix;
  }

  public void setMatrix(String[][] matrix) {
    this.matrix = matrix;
  }

  public Map<String, List<String>> getAppliedWinningCombinations() {
    return appliedWinningCombinations;
  }

  public void setAppliedWinningCombinations(
      Map<String, List<String>> appliedWinningCombinations) {
    this.appliedWinningCombinations = appliedWinningCombinations;
  }

  public String getAppliedBonusSymbol() {
    return appliedBonusSymbol;
  }

  public void setAppliedBonusSymbol(String appliedBonusSymbol) {
    this.appliedBonusSymbol = appliedBonusSymbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScratchGameEngine that = (ScratchGameEngine) o;
    return columns == that.columns && rows == that.rows && Objects.equals(symbols, that.symbols) && Objects.equals(
        probabilities, that.probabilities) && Objects.equals(winCombinations, that.winCombinations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(columns, rows, symbols, probabilities, winCombinations);
  }

  @Override
  public String toString() {
    return "GameConfig{" +
        "columns=" + columns +
        ", rows=" + rows +
        ", symbols=" + symbols +
        ", probabilities=" + probabilities +
        ", winCombinations=" + winCombinations +
        '}';
  }
}