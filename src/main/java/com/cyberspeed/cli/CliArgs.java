package com.cyberspeed.cli;

import com.cyberspeed.exception.CliArgumentException;
import com.cyberspeed.util.LoggingUtils;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The {@code CliArgs} class represents a set of command-line arguments.
 * It provides methods to retrieve argument values, check for the presence of keys,
 * and perform other operations on the arguments map. The class is immutable,
 * ensuring that the arguments cannot be modified after initialization.
 */
public final class CliArgs {

  // Get logger from LoggingUtils class for reuse
  private static final Logger logger = LoggingUtils.getLogger(CliArgs.class.getName());

  private final Map<String, String> arguments;

  /**
   * Constructs a {@code CliArgs} instance with the specified arguments map.
   * The map is made unmodifiable to ensure immutability.
   *
   * @param arguments the map of command-line arguments
   * @throws NullPointerException if the arguments map is null
   */
  public CliArgs(Map<String, String> arguments) {
    this.arguments = Collections.unmodifiableMap(Objects.requireNonNull(arguments, "Arguments map cannot be null"));
    logger.info("CliArgs initialized with arguments: " + arguments);
  }

  /**
   * Retrieves the value associated with the specified key.
   *
   * @param key the key of the argument to retrieve
   * @return the value associated with the key, or null if the key is not present
   */
  public String get(String key) {
    String value = arguments.get(key);
    logger.info(String.format("Retrieving argument with key '%s', value: %s", key, value));
    return value;
  }

  /**
   * Retrieves the value associated with the specified key, ensuring it is present.
   *
   * @param key the key of the required argument
   * @return the value associated with the key
   * @throws CliArgumentException if the key is not present
   */
  public String getRequired(String key) {
    if (!arguments.containsKey(key)) {
      logger.severe("Missing required argument: " + key);
      throw new CliArgumentException("Missing required argument: " + key);
    }
    String value = arguments.get(key);
    logger.info(String.format("Retrieving required argument with key '%s', value: %s", key, value));
    return value;
  }

  /**
   * Retrieves the value associated with the specified key, or returns a default value if absent.
   *
   * @param key the key of the argument to retrieve
   * @param defaultValue the default value to return if the key is not present
   * @return the value associated with the key, or the default value if the key is not present
   */
  public String getOrDefault(String key, String defaultValue) {
    String value = arguments.getOrDefault(key, defaultValue);
    logger.info(String.format("Retrieving argument with key '%s', returning default value if absent: %s", key, value));
    return value;
  }

  /**
   * Checks if the arguments contain the specified key.
   *
   * @param key the key to check for presence
   * @return true if the key is present, false otherwise
   */
  public boolean containsKey(String key) {
    boolean contains = arguments.containsKey(key);
    logger.info(String.format("Checking if arguments contain key '%s': %b", key, contains));
    return contains;
  }

  /**
   * Checks if the arguments map is empty.
   *
   * @return true if the arguments map is empty, false otherwise
   */
  public boolean isEmpty() {
    boolean empty = arguments.isEmpty();
    logger.info(String.format("Checking if arguments are empty: %b", empty));
    return empty;
  }

  /**
   * Retrieves the number of arguments.
   *
   * @return the size of the arguments map
   */
  public int size() {
    int size = arguments.size();
    logger.info(String.format("Retrieving the size of arguments: %d", size));
    return size;
  }

  /**
   * Retrieves all arguments as an unmodifiable map.
   *
   * @return the map of all arguments
   */
  public Map<String, String> getAll() {
    logger.info("Retrieving all arguments: " + arguments);
    return arguments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CliArgs cliArgs = (CliArgs) o;
    boolean equals = arguments.equals(cliArgs.arguments);
    logger.info(String.format("Checking equality with another CliArgs: %b", equals));
    return equals;
  }

  @Override
  public int hashCode() {
    int hashCode = Objects.hash(arguments);
    logger.info(String.format("Generating hash code for arguments: %d", hashCode));
    return hashCode;
  }

  @Override
  public String toString() {
    String stringRepresentation = "CliArgs{" + arguments + '}';
    logger.info("Converting arguments to string: " + stringRepresentation);
    return stringRepresentation;
  }
}