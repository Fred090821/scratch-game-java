package com.cyberspeed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class WinCombination {

  @JsonProperty("reward_multiplier")
  private double rewardMultiplier;
  private String when;
  private int count;
  private String group;
  @JsonProperty("covered_areas")
  private List<List<String>> coveredAreas;

  public double getRewardMultiplier() {
    return rewardMultiplier;
  }

  public void setRewardMultiplier(double rewardMultiplier) {
    this.rewardMultiplier = rewardMultiplier;
  }

  public String getWhen() {
    return when;
  }

  public void setWhen(String when) {
    if (when == null || when.trim().isEmpty()) {
      throw new IllegalArgumentException("When cannot be null or empty.");
    }
    this.when = when;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    if (group == null || group.trim().isEmpty()) {
      throw new IllegalArgumentException("Group cannot be null or empty.");
    }
    this.group = group;
  }

  public List<List<String>> getCoveredAreas() {
    return coveredAreas;
  }

  public void setCoveredAreas(List<List<String>> coveredAreas) {
    this.coveredAreas = coveredAreas;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WinCombination that = (WinCombination) o;
    return Double.compare(that.rewardMultiplier, rewardMultiplier) == 0 && count == that.count && Objects.equals(when,
        that.when) && Objects.equals(group, that.group) && Objects.equals(coveredAreas, that.coveredAreas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardMultiplier, when, count, group, coveredAreas);
  }

  @Override
  public String toString() {
    return "WinCombination{" +
        "rewardMultiplier=" + rewardMultiplier +
        ", when='" + when + '\'' +
        ", count=" + count +
        ", group='" + group + '\'' +
        ", coveredAreas=" + coveredAreas +
        '}';
  }
}