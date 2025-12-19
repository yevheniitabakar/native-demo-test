# Command Reference

## Build & Compile

```bash
./gradlew clean build -x test     # Compile without running tests
./gradlew clean build              # Full build with tests
./gradlew compileJava              # Java compilation only
./gradlew dependencies             # Show dependencies
./gradlew tasks                    # List available tasks
```

## Run Tests

```bash
./gradlew test                                  # All tests
./gradlew test --tests "LoginTest"              # Specific test class
./gradlew test --tests "LoginTest.validLogin"  # Specific test method
./gradlew test --tests "*Smoke*"                # Pattern match
./gradlew test --info                           # Verbose output
./gradlew test --debug                          # Debug mode
./gradlew test -x                               # Stop on first failure
```

## Reporting

```bash
./gradlew allureReport             # Generate Allure report
./gradlew allureServe              # Open Allure web server
./gradlew allureClean              # Clear Allure results
```

## Clean & Rebuild

```bash
./gradlew clean                    # Remove build directory
./gradlew cleanTest                # Clear test results
./gradlew clean build -x test      # Full clean rebuild (no tests)
```

## IDE Support

```bash
./gradlew idea                     # Generate IntelliJ IDEA project files
./gradlew eclipse                  # Generate Eclipse project files
```

## Tips

- Run `./gradlew` with `-x test` to skip test execution during build.
- Use `--tests` with regex patterns for flexible test selection.
- Add `--parallel` for parallel test execution (configure in `testng.xml`).
- Logs are in `logs/` directory (created after test run).

