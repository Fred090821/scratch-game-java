package com.cyberspeed.validator;

import com.cyberspeed.exception.CliArgumentException;
import com.cyberspeed.exception.InvalidBettingAmountException;
import com.cyberspeed.util.LoggingUtils;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The {@code CliArgsValidator} class provides utility methods for validating command-line arguments.
 * It ensures that arguments are correctly formatted, keys are non-empty and allowed, and values are
 * properly resolved. The class also validates specific argument values, such as ensuring the betting
 * amount is a positive integer. It logs validation steps and errors, throwing exceptions when validation
 * fails to enforce correct usage of command-line arguments.
 */
public class CliArgsValidator {

  private static final Logger LOGGER = LoggingUtils.getLogger(CliArgsValidator.class.getName());
  private static final String[] ALLOWED_KEYS = {"config", "betting-amount"};

  public static void validateFormat(String arg, int index) {
    if (!arg.startsWith("--")) {
      String message = String.format("Invalid argument format: '%s' at position %d", arg, index);
      LOGGER.warning(message);
      throw new CliArgumentException(message);
    }
    String message = String.format("Validated format for argument: '%s' at position %d", arg, index);
    LOGGER.info(message);
  }

  public static void validateKeyPartNotEmpty(String keyPart, int index) {
    if (keyPart.isEmpty()) {
      String message = String.format("Empty key at position %d", index);
      LOGGER.warning(message);
      throw new CliArgumentException(message);
    }
    String message = String.format("Validated non-empty key part at position %d", index);
    LOGGER.info(message);
  }

  public static void validateKeyNotEmpty(String key, String arg, int index) {
    if (key.isEmpty()) {
      String message = String.format("Empty key in argument '%s' at position %d", arg, index);
      LOGGER.warning(message);
      throw new CliArgumentException(message);
    }
    String message = String.format("Validated non-empty key for argument '%s' at position %d", arg, index);
    LOGGER.info(message);
  }

  public static void validateKeyAllowed(String key, int index) {
    if (!Arrays.asList(ALLOWED_KEYS).contains(key)) {
      String message = String.format("Invalid key '%s' at position %d. Allowed keys: --config, --betting-amount", key, index);
      LOGGER.warning(message);
      throw new CliArgumentException(message);
    }
    String message = String.format("Validated allowed key '%s' at position %d", key, index);
    LOGGER.info(message);
  }

  public static String resolveValue(String[] args, int currentIndex, String[] keyValueParts) {
    String value = "";
    if (keyValueParts.length == 2) {
      value = keyValueParts[1];
    } else if (currentIndex + 1 < args.length && !args[currentIndex + 1].startsWith("--")) {
      value = args[currentIndex + 1];
    }
    String message = String.format("Resolved value: '%s' for argument at index %d", value, currentIndex);
    LOGGER.info(message);
    return value;
  }

  public static void validateBettingAmount(int bettingAmount) {
    if (bettingAmount <= 0) {
      String message = String.format("Invalid betting amount: %d", bettingAmount);
      LOGGER.warning(message);
        throw new InvalidBettingAmountException("Betting amount must be a non-negative integer");
    }
    String message = String.format("Validated betting amount: %d", bettingAmount);
    LOGGER.info(message);
  }
}