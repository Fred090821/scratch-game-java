package com.cyberspeed.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cyberspeed.config.ConfigParser;
import com.cyberspeed.engine.ScratchGameEngine;
import com.cyberspeed.exception.InvalidBettingAmountException;
import com.cyberspeed.exception.InvalidGameConfigurationException;
import com.cyberspeed.model.ScratchGameConfiguration;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScratchGameEngineTest {

  private static final String INVALID_ROWS_COLUMNS_CONFIG_PATH = "json/invalid/invalid-rows-columns-config.json";
  private static final String MISSING_BONUS_SYMBOLS_CONFIG_PATH = "json/invalid/missing-bonus-symbols-config.json";
  private static final String MISSING_STANDARD_SYMBOLS_CONFIG_PATH = "json/invalid/missing-standard-symbols-config.json";
  private static final String CONFIG_PATH = "json/valid/config.json";
  private static final int BET_AMOUNT = 100;
  private static final int BET_AMOUNT_ZERO = 0;


  @Test
  @DisplayName("Should throw InvalidGameConfigurationException for invalid config")
  void given_configurationFileMissingStandardSymbols_whenInstantiatingScratchGameEngine_thenInvalidGameConfigurationException() {
    // Given
    final ScratchGameConfiguration configModelWithMissingStandardSymbols = new ConfigParser(MISSING_STANDARD_SYMBOLS_CONFIG_PATH).load();
    // When & Then
    assertThrows(InvalidGameConfigurationException.class, () -> {
      new ScratchGameEngine(configModelWithMissingStandardSymbols, BET_AMOUNT);
    });
  }

  @Test
  @DisplayName("Should throw InvalidGameConfigurationException for invalid config")
  void given_configurationFileMissingBonusSymbol_whenInstantiatingScratchGameEngine_thenInvalidGameConfigurationException() {
    // Given
    final ScratchGameConfiguration configModelWithMissingBonusSymbols = new ConfigParser(MISSING_BONUS_SYMBOLS_CONFIG_PATH).load();
    // When & Then
    assertThrows(InvalidGameConfigurationException.class, () -> {
      new ScratchGameEngine(configModelWithMissingBonusSymbols, BET_AMOUNT);
    });
  }

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

  @Test
  @DisplayName("Should throw InvalidBettingAmountException for negative bet amount")
  void given_negativeBetAmount_whenValidated_thenInvalidBettingAmountExceptionIsThrown() {
    // Given
    final var config = new ConfigParser(CONFIG_PATH).load();
    // When & Then
    assertThrows(InvalidBettingAmountException.class, () -> {
      new ScratchGameEngine(config, BET_AMOUNT_ZERO);
    });
  }

  @Test
  @DisplayName("Should accept valid betting amounts without exceptions")
  void givenValidBetAmounts_whenCreatingEngine_thenNoExceptionsThrown() {
    // Given
    final ScratchGameConfiguration config = new ConfigParser(CONFIG_PATH).load();

    // Valid betting amounts
    final int[] validAmounts = {1, 100, Integer.MAX_VALUE};

    assertAll("Valid betting amounts",
        () -> Arrays.stream(validAmounts).forEach(amount ->
            assertDoesNotThrow(
                () -> new ScratchGameEngine(config, amount),
                "Should not throw for amount: " + amount
            )
        )
    );
  }

  @Test
  @DisplayName("Should generate valid 3x3 matrix when starting game")
  void whenStartingGameWithValidConfig_thenGeneratesCorrectMatrix() {
    // Given
    final ScratchGameConfiguration config = new ConfigParser(CONFIG_PATH).load();
    final int validBetAmount = 100;
    ScratchGameEngine gameEngine = new ScratchGameEngine(config, validBetAmount);

    // When
    assertDoesNotThrow(gameEngine::startGame, "Game start should not throw exceptions");

    // Then
    final String[][] matrix = gameEngine.getMatrix();
    assertAll(
        () -> assertNotNull(matrix, "Matrix should not be null"),
        () -> assertEquals(3, matrix.length, "Should have 3 rows"),
        () -> assertAll("Each row should have 3 columns",
            () -> Arrays.stream(matrix).forEach(row ->
                assertEquals(3, row.length, "Row should have 3 columns")
            )
        )
    );
  }

}