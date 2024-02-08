[![Tests](https://github.com/aloarte/skintkvault/actions/workflows/ci.yml/badge.svg)](https://github.com/aloarte/skintkvault/actions/workflows/ci.yml)
[![Coverage Status](https://codecov.io/github/aloarte/skintkvault/coverage.svg?branch=master)](https://app.codecov.io/gh/aloarte/skintkvault)
# Skintkvault

Backend developed in Ktor, using Kotlin and Koin to provide services for the Skintker app.

## Stack
* Language: Kotlin - https://kotlinlang.org/
* Database abstraction layer: Exposed Framework - https://github.com/JetBrains/Exposed
* Dependency Injection: Koin - https://insert-koin.io/
* Build tool: Gradle - https://gradle.org/
* Test Mock framework: MockK - https://github.com/mockk/mockk
* Static code analyzer: Detekt - https://github.com/detekt/detekt
## API

The api provides methods to add or edit a new log, and query all the logs from a given user. Also single log or every logs can be removed from a given user.
Additionally, the stats of the given user can be queried.

More info can be found at the [Api Wiki](https://github.com/aloarte/skintkvault/wiki/API-Guide)

## Docker deployment
The application can be build through a Dockerfile into a docker image:
* **docker build -t skintktvault:1.0.0 .**

Then the GOOGLE_APPLICATION_CREDENTIALS environment variable must be provided. This is the same value found in the Dockerfile. Also the data needed to access to the database must be provided.
* **docker run --name skintkvault -e DDBB_USER= -e DDBB_PWD= -e DDBB_NAME= -e DDBB_PORT= -e DDBB_CONTAINER= -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/keys/skintker-firebase-adminsdk-9ed86-e8cbebaddb.json --network=skintktvault_network -d skintkvault:1.0.0**
