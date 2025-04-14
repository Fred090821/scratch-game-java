# Scratch Card Game (Java Implementation) #

A Java implementation of a scratch card game that generates game matrices, calculates rewards, and applies winning combinations based on JSON configuration files.

---

## Features
- Load game configuration from JSON files
- Validate CLI arguments and configuration files
- Generate game matrices using probability distributions
- Calculate rewards with winning combinations and bonus symbols
- Handle both standard and bonus symbols
- Comprehensive error handling and logging
- Swing-based graphical interface


## Design
- For each group, select the best combination (highest count).
- For each symbol, find all combinations (from different groups) that apply to it.
- For each symbol, multiply the multipliers of all applicable combinations.
- Multiply by the symbols base reward and sum across symbols.


## 🔑 Key Classes

| Class                      | Responsibility                              | Package                  |
|----------------------------|---------------------------------------------|--------------------------|
| `ScratchGameLauncher`      | Main entry point and GUI initialization     | `com.cyberspeed`         |
| `CliArgsParser`            | Parse/validate CLI arguments                | `com.cyberspeed.cli`     |
| `ScratchGameEngine`        | Generate matrices and calculate rewards     | `com.cyberspeed.service` |
| `ScratchGameConfiguration` | Map JSON config to Java objects             | `com.cyberspeed.config`  |
| `WinCombination`           | Define win conditions and multipliers       | `com.cyberspeed.model`   |



## 🛠️ CLI Arguments

### Required Arguments

| Argument             | Description                          | Required | Example       | Validation Rules              |
|----------------------|--------------------------------------|----------|---------------|--------------------------------|
| `--config`           | Path to JSON configuration file     | Yes      | `config.json` | - Valid file path<br>- File must exist |
| `--betting-amount`   | Betting amount in whole numbers     | Yes      | `100`         | - Integer > 0<br>- No decimals |

---

### Example Usage

**Valid Command**:
```bash
java -jar target/scratch-game-1.0.jar \
  --config .../config.json \
  --betting-amount 100
  
---

1 - run the command
2 - the GUI will open without any output and any input in the grid, only the bet amount is available
3 - click on Start Game, this will populate the 3x3 grid and output the result 
4 - if you want to play again with the same amount and same config file, click Start Game again, as much as you want.


On my local imac this how I run it:

jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % pwd           
/Users/jaydenassi/Documents/UAE
jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % mvn -v                                                                                                                                       
Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
Maven home: /opt/homebrew/Cellar/maven/3.9.9/libexec
Java version: 17.0.14, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk@17/17.0.14/libexec/openjdk.jdk/Contents/Home
Default locale: en_IE, platform encoding: UTF-8
OS name: "mac os x", version: "15.3.2", arch: "aarch64", family: "mac"
jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % java --version
openjdk 17.0.14 2025-01-21
OpenJDK Runtime Environment Homebrew (build 17.0.14+0)
OpenJDK 64-Bit Server VM Homebrew (build 17.0.14+0, mixed mode, sharing)
jaydenassi@iMac UAE % 
jaydenassi@iMac UAE % 

## Example Command
jaydenassi@iMac UAE % java -jar scratch-game-java-1.0-SNAPSHOT-jar-with-dependencies.jar --config /Users/jaydenassi/Documents/UAE/config.json --betting-amount 100
```


## ❌ Invalid Examples

### Incorrect CLI Usage
```bash
# Missing required --config argument
java -jar scratch-game.jar --betting-amount 100

# Negative betting amount
java -jar scratch-game.jar --config config.json --betting-amount -50

# Non-integer betting amount
java -jar scratch-game.jar --config config.json --betting-amount 99.99

# Invalid config file path
java -jar scratch-game.jar --config missing_file.json --betting-amount 100

# Extra unsupported argument
java -jar scratch-game.jar --config config.json --betting-amount 100 --invalid-arg foo

# Malformed argument syntax
java -jar scratch-game.jar -config config.json --bettingamount 100
  
```

## Requirements ##

- **JDK 17**
- **Maven Apache Maven 3.9.9**
- **jackson-core 2.18.1**
- **jackson-databind 2.18.1**
- **junit-jupiter-api 5.10.0**
- **junit-jupiter-engine 5.10.0**
- **maven-compiler-plugin 3.11.0**
- **maven-assembly-plugin 3.6.0**
- JSON configuration files (src/test/resources/json/valid/config.json)


## 👤 Author
**Fred Assi**
- Email: assifred2005@gmail.com