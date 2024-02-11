# Ktor Change Data Capture implementation

[Project Description and Overview]

## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)



## Installation

Provide instructions on how to install and set up your project.<br>
Include any prerequisites and steps needed to get the project up and running.<br>
For example:

To build and run the project locally, you need to have Java and Gradle installed.

1. Clone the repository:
   ```bash
   git clone https://github.com/UchePhilz/cdc-consumer.git
   ```
2. Navigate to the project directory:
   ```bash
   cd cdc-consumer
   ```
3. Build the project:
    ```bash
   ./gradlew run
    ```
## Configuration

Within this project you can find 3 configs in the resource dir
- application.conf: This contain a basic configuration for ktor
- kakfa.conf: This contains the configuration for the kafka consumer
- logback.xml: Log configuration. I added a filter to remove the noise from the console.

Project root directory

- debezium-connector.json json body for debezium connector
- docker-compose.yml docker compose file
 
## Usage



