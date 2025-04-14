package com.cyberspeed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayResult {

  @JsonProperty("matrix")
  private final String[][] matrix;

  @JsonProperty("reward")
  private final int reward;

  @JsonProperty("applied_winning_combinations")
  private final Map<String, List<String>> appliedWinningCombinations;

  @JsonProperty("applied_bonus_symbol")
  private final String appliedBonusSymbol;

  public PlayResult(
      String[][] matrix,
      int reward,
      Map<String, List<String>> appliedWinningCombinations,
      String appliedBonusSymbol
  ) {
    this.matrix = matrix;
    this.reward = reward;
    this.appliedWinningCombinations = appliedWinningCombinations;
    this.appliedBonusSymbol = appliedBonusSymbol;
  }

  // Getters (required for serialization)
  public String[][] getMatrix() {
    return matrix;
  }

  public int getReward() {
    return reward;
  }

  public Map<String, List<String>> getAppliedWinningCombinations() {
    return appliedWinningCombinations;
  }

  public String getAppliedBonusSymbol() {
    return appliedBonusSymbol;
  }

  @Override
  public String toString() {
    return "PlayResult{" +
        "matrix=" + Arrays.toString(matrix) +
        ", reward=" + reward +
        ", appliedWinningCombinations=" + appliedWinningCombinations +
        ", appliedBonusSymbol='" + appliedBonusSymbol + '\'' +
        '}';
  }
}