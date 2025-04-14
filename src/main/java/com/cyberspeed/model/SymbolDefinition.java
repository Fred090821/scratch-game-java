package com.cyberspeed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SymbolDefinition {

  @JsonProperty("reward_multiplier")
  private double rewardMultiplier;
  private String type;
  private String impact;
  private Integer extra;

  public SymbolDefinition() {
  }

  public double getRewardMultiplier() {
    return rewardMultiplier;
  }

  public void setRewardMultiplier(double rewardMultiplier) {
    this.rewardMultiplier = rewardMultiplier;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    if (type == null || type.trim().isEmpty()) {
      throw new IllegalArgumentException("Type cannot be null or empty.");
    }
    this.type = type;
  }

  public String getImpact() {
    return impact;
  }

  public void setImpact(String impact) {

    if (impact == null || impact.isEmpty()) {
      throw new IllegalArgumentException("Impact cannot be null or empty.");
    }
    this.impact = impact;
  }

  public Integer getExtra() {
    return extra;
  }

  public void setExtra(Integer extra) {
    this.extra = extra;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SymbolDefinition that = (SymbolDefinition) o;
    return Double.compare(that.rewardMultiplier, rewardMultiplier) == 0 && Objects.equals(type, that.type)
        && Objects.equals(impact, that.impact) && Objects.equals(extra, that.extra);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardMultiplier, type, impact, extra);
  }

  @Override
  public String toString() {
    return "SymbolDefinition{" +
        "rewardMultiplier=" + rewardMultiplier +
        ", type='" + type + '\'' +
        ", impact='" + impact + '\'' +
        ", extra=" + extra +
        '}';
  }
}

