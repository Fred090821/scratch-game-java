package com.cyberspeed.config;

import com.cyberspeed.exception.ConfigFileParsingException;
import com.cyberspeed.model.ScratchGameConfiguration;
import com.cyberspeed.util.LoggingUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The {@code ConfigParser} class is responsible for loading and parsing the configuration
 * for the Scratch Game application. It attempts to read the configuration from a specified
 * file path, first checking the filesystem and then the classpath if the file is not found.
 * The class uses Jackson's {@code ObjectMapper} to parse the configuration into a
 * {@code ScratchGameConfiguration} object. It also handles various exceptions that may
 * occur during the parsing process, such as JSON syntax errors or mapping issues, and
 * logs detailed error messages for troubleshooting.
 */
public class ConfigParser {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Logger logger = LoggingUtils.getLogger(ConfigParser.class.getName());

  private final String configFilePath;

  /**
   * Constructs a {@code ConfigParser} with the specified configuration file path.
   * Validates the provided path to ensure it is not null or empty.
   *
   * @param configFilePath the path to the configuration file
   * @throws ConfigFileParsingException if the path is null or empty
   */
  public ConfigParser(String configFilePath) {
    if (configFilePath == null || configFilePath.isEmpty()) {
      logger.severe("Configuration file path must not be null or empty");
      throw new ConfigFileParsingException("Configuration file path must not be null or empty");
    }
    this.configFilePath = validatePath(configFilePath);
  }

  /**
   * Validates the configuration file path.
   * Ensures the path is not null or blank.
   *
   * @param path the path to validate
   * @return the validated path
   * @throws ConfigFileParsingException if the path is null or blank
   */
  private static String validatePath(String path) {
    Objects.requireNonNull(path, "Configuration path must not be null");
    if (path.isBlank()) {
      Logger.getLogger(ConfigParser.class.getName()).severe("Configuration path is blank");
      throw new ConfigFileParsingException("Configuration path must not be blank");
    }
    return path;
  }

  /**
   * Loads the configuration from the specified file path.
   * Attempts to read the configuration from the filesystem first, then the classpath.
   *
   * @return the parsed {@code ScratchGameConfiguration} object
   * @throws ConfigFileParsingException if the configuration cannot be loaded or parsed
   */
  public ScratchGameConfiguration load() {
    logger.info("Loading configuration from: " + configFilePath);

    File configFile = new File(configFilePath);
    if (configFile.exists()) {
      logger.info("Found config file on filesystem: " + configFile.getAbsolutePath());
      try {
        ScratchGameConfiguration config = OBJECT_MAPPER.readValue(configFile, ScratchGameConfiguration.class);
        logger.info("Successfully parsed configuration from filesystem");
        return config;
      } catch (IOException e) {
        logger.severe("Failed to read config file from filesystem: " + e.getMessage());
        throw new ConfigFileParsingException(
            String.format("Failed to read config file %s: %s", configFilePath, e.getMessage()), e
        );
      }
    }

    logger.info("Config file not found on filesystem. Trying classpath...");

    try (InputStream inputStream = ScratchGameConfiguration.class.getClassLoader().getResourceAsStream(configFilePath)) {

      if (inputStream == null) {
        logger.severe("Config file not found in classpath: " + configFilePath);
        throw new ConfigFileParsingException("Config file not found: " + configFilePath);
      }

      ScratchGameConfiguration config = OBJECT_MAPPER.readValue(inputStream, ScratchGameConfiguration.class);
      logger.info("Successfully parsed configuration from classpath resource");
      return config;

    } catch (JsonParseException e) {
      String location = String.format("Line %d, Column %d", e.getLocation().getLineNr(), e.getLocation().getColumnNr());
      logger.severe(String.format("Invalid JSON syntax in %s at %s: %s", configFilePath, location, e.getOriginalMessage()));
      throw new ConfigFileParsingException(
          String.format("Invalid JSON syntax in %s at %s: %s", configFilePath, location, e.getOriginalMessage()), e
      );
    } catch (JsonMappingException e) {
      String path = e.getPath().stream()
          .map(Reference::getFieldName)
          .collect(Collectors.joining("."));
      logger.severe(String.format("JSON mapping error in %s at path '%s': %s", configFilePath, path, e.getOriginalMessage()));
      throw new ConfigFileParsingException(
          String.format("JSON mapping error in %s at path '%s': %s", configFilePath, path, e.getOriginalMessage()), e
      );
    } catch (IOException e) {
      logger.severe("IOException while reading config file: " + e.getMessage());
      throw new ConfigFileParsingException(
          String.format("Failed to read config file %s: %s", configFilePath, e.getMessage()), e
      );
    }
  }
}