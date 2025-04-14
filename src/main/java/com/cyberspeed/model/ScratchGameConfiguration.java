package com.cyberspeed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class ScratchGameConfiguration {

  private int columns;
  private int rows;
  private Map<String, SymbolDefinition> symbols;
  private Probabilities probabilities;
  @JsonProperty("win_combinations")
  private Map<String, WinCombination> winCombinations;

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
    if (symbols == null || symbols.isEmpty()) {
      throw new IllegalArgumentException("Symbols map cannot be null or empty.");
    }

    for (String symbol : symbols.keySet()) {
      if (symbol == null || symbol.trim().isEmpty()) {
        throw new IllegalArgumentException("Key in symbols map cannot be null or empty.");
      }
    }

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

  @Override
  public String toString() {
    return "ScratchGameConfiguration{" +
        "columns=" + columns +
        ", rows=" + rows +
        ", symbols=" + symbols +
        ", probabilities=" + probabilities +
        ", winCombinations=" + winCombinations +
        '}';
  }
}