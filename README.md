# UI Automation Framework - Highline Warren

This project is a basic UI automation framework created as part of the coding exercise. It covers a few key scenarios from the Highline Warren website.

## Tech Stack
- Java
- Selenium WebDriver
- TestNG
- BrowserStack (for cross-browser execution)
- ExtentReports (for reporting)
- Maven (for dependency management)

## Project Structure
- `tests/` → Test classes
- `pages/` → Page Object Model classes
- `framework/` → Base classes, config, and utilities
- `testng.xml` → Test Suite XML file

## Scenarios Automated
- Home Page UI element validation
- Browse by Category and Product Flow validation
- User Registration Flow

## How to Run the Tests
Prerequisites: Java 17, Maven 3.x installed.
1. Clone the repository.
2. Set your BrowserStack credentials inside the `browserstack.properties` file.
3. Run the tests by executing the `testng.xml` file.

> **Note:** All tests are configured to run on BrowserStack. No local browser execution setup is required.

## Test Reports
- After execution, the ExtentReports HTML report will be generated at:


## Code Delivery
- The complete code is pushed to GitHub (link shared separately).
- Test execution screenshots and Extent Report screenshots are also shared.
- Refer to the `/screenshots/` folder for execution evidence.

## Author
- Yashavantha N