package com.cyberspeed.model;

import java.util.Map;
import java.util.Objects;


public class StandardSymbolProbability {

  private int column;
  private int row;
  private Map<String, Integer> symbols;

  public StandardSymbolProbability() {
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public Map<String, Integer> getSymbols() {
    return symbols;
  }

  public void setSymbols(Map<String, Integer> symbols) {
    if (symbols == null || symbols.isEmpty()) {
      throw new IllegalArgumentException("Symbols map cannot be null or empty.");
    }

    // Validate that no key is null or an empty string
    for (String key : symbols.keySet()) {
      if (key == null || key.trim().isEmpty()) {
        throw new IllegalArgumentException("Key in symbols map cannot be null or empty.");
      }
    }
    this.symbols = symbols;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StandardSymbolProbability that = (StandardSymbolProbability) o;
    return column == that.column && row == that.row && Objects.equals(symbols, that.symbols);
  }

  @Override
  public int hashCode() {
    return Objects.hash(column, row, symbols);
  }

  @Override
  public String toString() {
    return "StandardSymbolProbability{" +
        "column=" + column +
        ", row=" + row +
        ", symbols=" + symbols +
        '}';
  }
}
