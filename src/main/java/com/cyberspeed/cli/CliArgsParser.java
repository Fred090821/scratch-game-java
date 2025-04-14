package com.cyberspeed.cli;

import com.cyberspeed.util.LoggingUtils;
import com.cyberspeed.validator.CliArgsValidator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The {@code CliArgsParser} class is responsible for parsing command-line arguments
 * into a structured format. It processes an array of argument strings, validates
 * their format and content using the {@code CliArgsValidator}, and constructs a
 * {@code CliArgs} object containing the parsed key-value pairs. The class ensures
 * that each argument is correctly formatted and that keys and values are properly
 * extracted and validated.
 */
public final class CliArgsParser {

    private static final Logger logger = LoggingUtils.getLogger(CliArgsParser.class.getName());

    public CliArgs parse(String[] args) {
        Map<String, String> parsedArgs = new LinkedHashMap<>();
        logger.info("Starting argument parsing...");

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            logger.info(String.format("Processing argument at index %d: %s", i, arg));

            CliArgsValidator.validateFormat(arg, i);

            String keyPart = arg.substring(2);
            logger.info(String.format("Extracted key part: %s", keyPart));
            CliArgsValidator.validateKeyPartNotEmpty(keyPart, i);

            String[] keyValue = keyPart.split("=", 2);
            String key = keyValue[0];
            logger.info(String.format("Extracted key: %s", key));

            CliArgsValidator.validateKeyAllowed(key, i);
            CliArgsValidator.validateKeyNotEmpty(key, arg, i);

            String value = CliArgsValidator.resolveValue(args, i, keyValue);
            logger.info(String.format("Resolved value for key '%s': %s", key, value));

            if (keyValue.length != 2 && i + 1 < args.length && !args[i + 1].startsWith("--")) {
                i++;
                logger.info(String.format("Next argument used as value for key '%s': %s", key, args[i]));
            }
            parsedArgs.put(key, value);
        }

        logger.info(String.format("Argument parsing completed. Parsed arguments: %s", parsedArgs));
        return new CliArgs(parsedArgs);
    }
}