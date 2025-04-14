package com.cyberspeed.model;

import com.cyberspeed.exception.InvalidGameConfigurationException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class BonusSymbolProbability {

  private Map<String, Integer> symbols;

  public BonusSymbolProbability() {
  }

  public BonusSymbolProbability(Map<String, Integer> symbols) {
    if (symbols == null || symbols.isEmpty()) {
      throw new IllegalArgumentException("Symbols map cannot be null or empty");
    }
    this.symbols = Collections.unmodifiableMap(symbols);
  }

  public Map<String, Integer> getSymbols() {
    if (symbols == null || symbols.isEmpty()) {
      throw new InvalidGameConfigurationException("Symbols map cannot be null or empty");
    }
    return symbols;
  }

  public void setSymbols(Map<String, Integer> symbols) {

    if (symbols == null || symbols.isEmpty()) {
      throw new IllegalArgumentException("Symbols map cannot be null or empty");
    }
    this.symbols = Collections.unmodifiableMap(symbols);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BonusSymbolProbability that = (BonusSymbolProbability) o;
    return Objects.equals(symbols, that.symbols);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbols);
  }

  @Override
  public String toString() {
    return "BonusSymbolProbability{" +
        "symbols=" + symbols +
        '}';
  }
}
