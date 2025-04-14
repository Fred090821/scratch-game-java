package com.cyberspeed.model;

import com.cyberspeed.exception.InvalidGameConfigurationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class Probabilities {

  @JsonProperty("standard_symbols")
  private List<StandardSymbolProbability> standardSymbols;

  @JsonProperty("bonus_symbols")
  private BonusSymbolProbability bonusSymbols;

  public Probabilities() {
  }

  public List<StandardSymbolProbability> getStandardSymbols() {
    return standardSymbols;
  }

  public void setStandardSymbols(List<StandardSymbolProbability> standardSymbols) {
    if (standardSymbols == null || standardSymbols.isEmpty()) {
      throw new InvalidGameConfigurationException("Standard symbols list cannot be null or empty");
    }
    this.standardSymbols = standardSymbols;
  }

  public BonusSymbolProbability getBonusSymbols() {
    return bonusSymbols;
  }

  public void setBonusSymbols(BonusSymbolProbability bonusSymbols) {
    if (bonusSymbols == null) {
      throw new InvalidGameConfigurationException("Bonus symbols cannot be null");
    }
    this.bonusSymbols = bonusSymbols;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Probabilities that = (Probabilities) o;
    return Objects.equals(standardSymbols, that.standardSymbols) && Objects.equals(bonusSymbols, that.bonusSymbols);
  }

  @Override
  public int hashCode() {
    return Objects.hash(standardSymbols, bonusSymbols);
  }

  @Override
  public String toString() {
    return "Probabilities{" +
        "standardSymbols=" + standardSymbols +
        ", bonusSymbols=" + bonusSymbols +
        '}';
  }
}
