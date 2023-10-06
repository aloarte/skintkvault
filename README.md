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

Then the GOOGLE_APPLICATION_CREDENTIALS environment variable must be provided. This is the same value found in the Dockerfile.
* **docker run --name skintktvault -p8080:8080 -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/keys/skintker-firebase-adminsdk-9ed86-e8cbebaddb.json skintktvault:1.0.0**