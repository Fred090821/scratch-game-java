package com.cyberspeed.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cyberspeed.config.ConfigParser;
import com.cyberspeed.engine.ScratchGameEngine;
import com.cyberspeed.exception.InvalidGameConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScratchGameEngineInvalidRowsAndColumnsConfigFileTest {

    private static final String INVALID_ROWS_COLUMNS_CONFIG_PATH = "json/invalid/invalid-rows-columns-config.json";
    private static final int BET_AMOUNT = 100;
    private ScratchGameEngine gameEngine;


  @Test
  @DisplayName("Should throw InvalidGameConfigurationException for negative bet amount")
  void given_negativeBetAmount_whenValidated_thenInvalidGameConfigurationExceptionIsThrown() {
    // Given
    final var invalidConfig = new ConfigParser(INVALID_ROWS_COLUMNS_CONFIG_PATH).load();
    // When & Then
    assertThrows(InvalidGameConfigurationException.class, () -> {
      new ScratchGameEngine(invalidConfig, BET_AMOUNT);
    });
  }

}