package com.cyberspeed.config;

import com.cyberspeed.exception.InvalidGameConfigurationException;
import com.cyberspeed.model.ScratchGameConfiguration;
import java.util.List;
import java.util.Map;

/**
 * Utility class for validating the game configuration model.
 * This class provides methods to ensure that the game configuration
 * adheres to the required specifications, such as grid dimensions
 * and the presence of necessary game components.
 */
public final class GameConfigModelValidator {

    private static final int REQUIRED_COLUMNS = 3;
    private static final int REQUIRED_ROWS = 3;

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     */
    private GameConfigModelValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Validates the grid dimensions of the game configuration.
     * Ensures that the configuration has the required number of columns and rows.
     *
     * @param config the game configuration to validate
     * @throws InvalidGameConfigurationException if the grid dimensions are incorrect
     */
    public static void validateGridDimensions(ScratchGameConfiguration config) {
        if (config.getColumns() != REQUIRED_COLUMNS || config.getRows() != REQUIRED_ROWS) {
            throw new InvalidGameConfigurationException(
                String.format("Game configuration must have %d columns and %d rows (found columns=%d, rows=%d)",
                    REQUIRED_COLUMNS, REQUIRED_ROWS, config.getColumns(), config.getRows())
            );
        }
    }

    /**
     * Validates the essential components of the game configuration.
     * Checks for the presence of symbols, win combinations, and probabilities.
     *
     * @param config the game configuration to validate
     * @throws InvalidGameConfigurationException if any required component is missing or empty
     */
    public static void validateGameComponents(ScratchGameConfiguration config) {
        requireNonNull(config, "Game configuration cannot be null");

        requireNonEmpty(config.getSymbols(), "Symbols map cannot be null or empty");
        requireNonEmpty(config.getWinCombinations(), "Win combinations cannot be null or empty");

        var probabilities = config.getProbabilities();
        requireNonNull(probabilities, "Probabilities cannot be null");

        requireNonEmpty(probabilities.getStandardSymbols(), "Standard symbol probabilities cannot be null or empty");

        var bonusSymbols = probabilities.getBonusSymbols();
        requireNonNull(bonusSymbols, "Bonus symbols cannot be null");
        requireNonEmpty(bonusSymbols.getSymbols(), "Bonus symbol probabilities cannot be null or empty");
    }

    /**
     * Ensures that the provided object is not null.
     *
     * @param obj the object to check
     * @param message the exception message if the object is null
     * @throws InvalidGameConfigurationException if the object is null
     */
    private static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new InvalidGameConfigurationException(message);
        }
    }

    /**
     * Ensures that the provided map is not null or empty.
     *
     * @param map the map to check
     * @param message the exception message if the map is null or empty
     * @throws InvalidGameConfigurationException if the map is null or empty
     */
    private static void requireNonEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new InvalidGameConfigurationException(message);
        }
    }

    /**
     * Ensures that the provided list is not null or empty.
     *
     * @param list the list to check
     * @param message the exception message if the list is null or empty
     * @throws InvalidGameConfigurationException if the list is null or empty
     */
    private static void requireNonEmpty(List<?> list, String message) {
        if (list == null || list.isEmpty()) {
            throw new InvalidGameConfigurationException(message);
        }
    }

}
