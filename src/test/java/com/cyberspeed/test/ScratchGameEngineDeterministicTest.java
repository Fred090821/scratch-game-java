package com.cyberspeed.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cyberspeed.config.ConfigParser;
import com.cyberspeed.engine.ScratchGameEngine;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScratchGameEngineDeterministicTest {

    private static final String TEST_A_MISS_BONUS_CONFIG_PATH = "json/deterministic/test-symbol_A_deterministic-win-with-miss-bonus.json";
    private static final String TEST_A_MULTIPLY_REWARD_CONFIG_FIVE_X_PATH = "json/deterministic/test-symbol_A_deterministic-win-with-multiply_reward_5x.json";
    private static final String TEST_A_MULTIPLY_REWARD_CONFIG_TEN_X_PATH = "json/deterministic/test-symbol_A_deterministic-win-with-multiply_reward_10x.json";

    private static final String TEST_A_EXTRA_BONUS_PLUS500_CONFIG_PATH = "json/deterministic/test-symbol_A_deterministic-win-with-multiply_reward_extra-bonus_+500.json";
    private static final String TEST_A_EXTRA_BONUS_PLUS1000_CONFIG_PATH = "json/deterministic/test-symbol_A_deterministic-win-with-multiply_reward_extra-bonus_+1000.json";

    private static final int BET_AMOUNT = 100;
    private ScratchGameEngine gameEngine;

    @Test
    @DisplayName("Should calculate correct reward with fixed configuration for symbol A win with MISS bonus")
    void givenSymbolA_whenGameStarts_thenGuaranteedWinWithMissBonus() {
        // When
        final var config = new ConfigParser(TEST_A_MISS_BONUS_CONFIG_PATH).load();
        gameEngine = new ScratchGameEngine(config, BET_AMOUNT);
        gameEngine.startGame();

        // Then - Verify matrix composition
        final var matrix = gameEngine.getMatrix();
        assertAll(
            () -> assertEquals(3, matrix.length, "Should have 3 rows"),
            () -> assertEquals(3, matrix[0].length, "Should have 3 columns")
        );

        // Verify symbol distribution
        final var aCount = countSymbol(matrix, "A");
        final var bonusCount = countSymbol(matrix, "MISS");
        assertAll(
            () -> assertEquals(8, aCount, "Should have 8 standard symbols"),
            () -> assertEquals(1, bonusCount, "Should have 1 bonus symbol")
        );

        // Verify winning combinations
        final var combinations = gameEngine.getAppliedWinningCombinations();
        assertAll(
            () -> assertTrue(combinations.containsKey("A"), "Should have winning combinations for A"),
            () -> assertEquals(3, combinations.get("A").size(), "Should have 2 combinations for A"),
            () -> assertIterableEquals(
                List.of("same_symbol_8_times", "same_symbols_horizontally", "same_symbols_vertically"),
                combinations.get("A").stream().sorted().collect(Collectors.toList()),
                "Should have correct combination names"
            )
        );

        // Verify final reward calculation
        final var expectedReward = BET_AMOUNT * 5 * 10 * 2 * 2;
        assertEquals(expectedReward, gameEngine.getReward(),
            "Should calculate correct reward based on configuration");
    }

    @Test
    @DisplayName("Should calculate correct reward with fixed configuration for symbol A win with 5x")
    void givenSymbolA_whenGameStarts_thenGuaranteedWinWithMultiplyReward5x() {
        // When
        final var config = new ConfigParser(TEST_A_MULTIPLY_REWARD_CONFIG_FIVE_X_PATH).load();
        gameEngine = new ScratchGameEngine(config, BET_AMOUNT);
        gameEngine.startGame();

        // Then - Verify matrix composition
        final var matrix = gameEngine.getMatrix();
        assertAll(
            () -> assertEquals(3, matrix.length, "Should have 3 rows"),
            () -> assertEquals(3, matrix[0].length, "Should have 3 columns")
        );

        // Verify symbol distribution
        final var aCount = countSymbol(matrix, "A");
        final var bonusCount = countSymbol(matrix, "5x");
        assertAll(
            () -> assertEquals(8, aCount, "Should have 8 standard symbols"),
            () -> assertEquals(1, bonusCount, "Should have 1 bonus symbol")
        );

        // Verify winning combinations
        final var combinations = gameEngine.getAppliedWinningCombinations();
        assertAll(
            () -> assertTrue(combinations.containsKey("A"), "Should have winning combinations for A"),
            () -> assertEquals(3, combinations.get("A").size(), "Should have 2 combinations for A"),
            () -> assertIterableEquals(
                List.of("same_symbol_8_times", "same_symbols_horizontally", "same_symbols_vertically"),
                combinations.get("A").stream().sorted().collect(Collectors.toList()),
                "Should have correct combination names"
            )
        );

        // Verify final reward calculation
        final var expectedReward = BET_AMOUNT * 5 * 10 * 2 * 2 * 5;
        assertEquals(expectedReward, gameEngine.getReward(),
            "Should calculate correct reward based on configuration");
    }

    @Test
    @DisplayName("Should calculate correct reward with fixed configuration for symbol A win with 10x")
    void givenSymbolA_whenGameStarts_thenGuaranteedWinWithMultiplyReward10x() {
        // When
        final var config = new ConfigParser(TEST_A_MULTIPLY_REWARD_CONFIG_TEN_X_PATH).load();
        gameEngine = new ScratchGameEngine(config, BET_AMOUNT);
        gameEngine.startGame();

        // Then - Verify matrix composition
        final var matrix = gameEngine.getMatrix();
        assertAll(
            () -> assertEquals(3, matrix.length, "Should have 3 rows"),
            () -> assertEquals(3, matrix[0].length, "Should have 3 columns")
        );

        // Verify symbol distribution
        final var aCount = countSymbol(matrix, "A");
        final var bonusCount = countSymbol(matrix, "10x");
        assertAll(
            () -> assertEquals(8, aCount, "Should have 8 standard symbols"),
            () -> assertEquals(1, bonusCount, "Should have 1 bonus symbol")
        );

        // Verify winning combinations
        final var combinations = gameEngine.getAppliedWinningCombinations();
        assertAll(
            () -> assertTrue(combinations.containsKey("A"), "Should have winning combinations for A"),
            () -> assertEquals(3, combinations.get("A").size(), "Should have 2 combinations for A"),
            () -> assertIterableEquals(
                List.of("same_symbol_8_times", "same_symbols_horizontally", "same_symbols_vertically"),
                combinations.get("A").stream().sorted().collect(Collectors.toList()),
                "Should have correct combination names"
            )
        );

        // Verify final reward calculation
        final var expectedReward = BET_AMOUNT * 5 * 10 * 2 * 2 * 10;
        assertEquals(expectedReward, gameEngine.getReward(),
            "Should calculate correct reward based on configuration");
    }

    @Test
    @DisplayName("Should calculate correct reward with fixed configuration for symbol A win with extra bonus +1000")
    void givenSymbolA_whenGameStarts_thenGuaranteedWinWithExtraBonus_plus1000() {
        // When
        final var config = new ConfigParser(TEST_A_EXTRA_BONUS_PLUS1000_CONFIG_PATH).load();
        gameEngine = new ScratchGameEngine(config, BET_AMOUNT);
        gameEngine.startGame();

        // Then - Verify matrix composition
        final var matrix = gameEngine.getMatrix();
        assertAll(

            () -> assertNotEquals(4, matrix.length, "Should have 3 rows"),
            () -> assertEquals(3, matrix.length, "Should have 3 rows"),
            () -> assertNotEquals(4, matrix[0].length, "Should have 3 columns"),
            () -> assertEquals(3, matrix[0].length, "Should have 3 columns")
        );

        // Verify symbol distribution
        final var aCount = countSymbol(matrix, "A");
        final var bonusCount = countSymbol(matrix, "+1000");
        assertAll(
            () -> assertEquals(8, aCount, "Should have 8 standard symbols"),
            () -> assertEquals(1, bonusCount, "Should have 1 bonus symbol")
        );

        // Verify winning combinations
        final var combinations = gameEngine.getAppliedWinningCombinations();
        assertAll(
            () -> assertTrue(combinations.containsKey("A"), "Should have winning combinations for A"),
            () -> assertEquals(3, combinations.get("A").size(), "Should have 2 combinations for A"),
            () -> assertIterableEquals(
                List.of("same_symbol_8_times", "same_symbols_horizontally", "same_symbols_vertically"),
                combinations.get("A").stream().sorted().collect(Collectors.toList()),
                "Should have correct combination names"
            )
        );

        // Verify final reward calculation
        final var expectedReward = BET_AMOUNT * 5 * 10 * 2 * 2 + 1000;
        assertEquals(expectedReward, gameEngine.getReward(),
            "Should calculate correct reward based on configuration");
    }

    @Test
    @DisplayName("Should calculate correct reward with fixed configuration for symbol A win with extra bonus +500")
    void givenSymbolA_whenGameStarts_thenGuaranteedWinWithExtraBonus_plus500() {
        // When
        final var config = new ConfigParser(TEST_A_EXTRA_BONUS_PLUS500_CONFIG_PATH).load();
        gameEngine = new ScratchGameEngine(config, BET_AMOUNT);
        gameEngine.startGame();

        // Then - Verify matrix composition
        final var matrix = gameEngine.getMatrix();
        assertAll(
            () -> assertEquals(3, matrix.length, "Should have 3 rows"),
            () -> assertEquals(3, matrix[0].length, "Should have 3 columns")
        );

        // Verify symbol distribution
        final var aCount = countSymbol(matrix, "A");
        final var bonusCount = countSymbol(matrix, "+500");
        assertAll(
            () -> assertEquals(8, aCount, "Should have 8 standard symbols"),
            () -> assertEquals(1, bonusCount, "Should have 1 bonus symbol")
        );

        // Verify winning combinations
        final var combinations = gameEngine.getAppliedWinningCombinations();
        assertAll(
            () -> assertTrue(combinations.containsKey("A"), "Should have winning combinations for A"),
            () -> assertEquals(3, combinations.get("A").size(), "Should have 2 combinations for A"),
            () -> assertIterableEquals(
                List.of("same_symbol_8_times", "same_symbols_horizontally", "same_symbols_vertically"),
                combinations.get("A").stream().sorted().collect(Collectors.toList()),
                "Should have correct combination names"
            )
        );

        // Verify final reward calculation
        final var expectedReward = BET_AMOUNT * 5 * 10 * 2 * 2 + 500;
        assertEquals(expectedReward, gameEngine.getReward(),
            "Should calculate correct reward based on configuration");
    }

    private long countSymbol(String[][] matrix, String symbol) {
        return Arrays.stream(matrix)
            .flatMap(Arrays::stream)
            .filter(s -> s.equals(symbol))
            .count();
    }
}