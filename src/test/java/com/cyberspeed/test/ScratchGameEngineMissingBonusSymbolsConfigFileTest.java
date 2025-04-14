package com.cyberspeed.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cyberspeed.config.ConfigParser;
import com.cyberspeed.engine.ScratchGameEngine;
import com.cyberspeed.exception.InvalidGameConfigurationException;
import com.cyberspeed.model.ScratchGameConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScratchGameEngineMissingBonusSymbolsConfigFileTest {

    private static final String INVALID_ROWS_COLUMNS_CONFIG_PATH = "json/invalid/invalid-rows-columns-config.json";
    private static final String MISSING_BONUS_SYMBOLS_CONFIG_PATH = "json/invalid/missing-bonus-symbols-config.json";
    private static final int BET_AMOUNT = 100;

  @Test
  @DisplayName("Should throw InvalidGameConfigurationException for negative bet amount")
  void given_missingBonusSymbols_whenValidated_thenInvalidGameConfigurationExceptionIsThrown() {
    // Given
    final ScratchGameConfiguration configModelWithMissingBonusSymbols = new ConfigParser(MISSING_BONUS_SYMBOLS_CONFIG_PATH).load();

    // When & Then
    assertThrows(InvalidGameConfigurationException.class, () -> {
      ScratchGameEngine gameEngine = new ScratchGameEngine(configModelWithMissingBonusSymbols, BET_AMOUNT);
      gameEngine.startGame();
      gameEngine.getMatrix();

    });
  }

  @Test
  @DisplayName("Should throw InvalidGameConfigurationException for invalid rows and columns")
  void given_invalidConfigFile_withInvalidRowsAndColumns_thenInvalidGameConfigurationExceptionIsThrown() {
    // Given
    final ScratchGameConfiguration invalidRowsAndColumnsConfigModel = new ConfigParser(INVALID_ROWS_COLUMNS_CONFIG_PATH).load();

    // When & Then
    assertThrows(InvalidGameConfigurationException.class, () -> {
      new ScratchGameEngine(invalidRowsAndColumnsConfigModel, BET_AMOUNT);
    });
  }

}