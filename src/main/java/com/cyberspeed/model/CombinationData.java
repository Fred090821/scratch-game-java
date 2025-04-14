package com.cyberspeed.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class CombinationData {

  private final String group;
  private final Set<String> symbols;

  public CombinationData(String group, Set<String> symbols) {
    Objects.requireNonNull(group, "Group cannot be null");
    if (symbols == null || symbols.isEmpty()) {
      throw new IllegalArgumentException("Symbols map cannot be null or empty");
    }
    this.group = group;
    this.symbols = Collections.unmodifiableSet(symbols);
  }

  public String getGroup() {
    return group;
  }

  public Set<String> getSymbols() {
    return symbols;
  }
}