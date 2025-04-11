![Download](https://img.shields.io/maven-central/v/com.infinum.buggy/buggy)  [![Build Status](https://app.bitrise.io/app/2f579e1f-5eb6-4bae-974c-3af8b0ff2a4c/status.svg?token=YSdaK3xxLACRNQCFFbtuqA&branch=main)](https://app.bitrise.io/app/2f579e1f-5eb6-4bae-974c-3af8b0ff2a4c) 

### <img align="left" src="resources/logo.svg" width="48">

# Android Buggy

## Description

An Android library for exporting simple resources (usually logs and debug data). It allows
you to add, remove and process resources before an export.

The project is organized in the following modules:

- `buggy` - Core module for adding, processing and exporting resources (logs and debug data in most
  cases).
- `buggy-android` - Module for providing Android specific details about device and app
  as `BuggyResource`.
- `buggy-rolling-file-logger` - Module for writing logs to files with a rolling strategy (once
  maximum total capacity is exceeded oldest files get deleted).
- `buggy-timber` - Module that provides a Timber tree in which custom loggers can be injected.


## Table of contents

* [Getting started](#getting-started)
* [Usage](#usage)
* [Requirements](#requirements)
* [Contributing](#contributing)
* [License](#license)
* [Credits](#credits)

## Getting started

To include _Buggy_ in your project, you have to add buildscript dependencies in your project
level `build.gradle` or `build.gradle.kts`:

**Groovy**

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}
```

**KotlinDSL**

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
}
```

Then, you can include the library in your module's `build.gradle` or `build.gradle.kts`:

**Groovy**

```groovy
implementation "com.infinum.buggy:buggy:0.1.0"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.buggy:buggy:0.1.0")
```

If you want to use other modules, you can include them in the same way:

**Groovy**

```groovy
implementation "com.infinum.buggy:buggy-android:0.1.0"
implementation "com.infinum.buggy:buggy-rolling-file-logger:0.1.0"
implementation "com.infinum.buggy:buggy-timber:0.1.0"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.buggy:buggy-android:0.1.0")
implementation("com.infinum.buggy:buggy-rolling-file-logger:0.1.0")
implementation("com.infinum.buggy:buggy-timber:0.1.0")
```

Don't forget to sync your project.

## Usage

To use the library, you have to initialize it with Builder. You can add resources (for example log
files) and processors (for example `EncryptionBuggyResourceProcessor`) to the builder and then build
it.
It is important to note that **the order of adding processors is important**. They will be applied to resources in the same order in which they were added when exporting.

The builder will return a `Buggy` instance which you can use to export resources.

```kotlin
Buggy.Builder()
    .add(add(FileBuggyResource(file)))
    .add(
        EncryptionBuggyResourceProcessor(
            keyCipher = rsaCipher,
            resourceCipher = aesCipher,
        )
    )
    .build()
```


To use the `Buggy` instance, you can call the `export` method with an `Exporter` instance. Then
library will export all resources processed by defined processors with exporter defined strategy.
Example of exporting resources to a zip file:

```kotlin
buggy.export(
    ZipBuggyExporter(
        file = report,
    ),
)
```

Check the [sample app](sample) for more detailed examples.

### Specific usage info

Library also provides different modules for specific use cases.


`buggy-android` module provides, among other things, `BuggyResource` implementations for Android
specific details about device and app.

```kotlin
Buggy.Builder()
    .add(ApplicationInfoBuggyResource(context))
    // ... other resources and processors
    .build()
```


`buggy-rolling-file-logger` module provides a `BuggyFileRollingLogger` which can be used to write logs to
files with a rolling strategy.

```kotlin 
val fileFactory = BuggyLimitedFileFactory(
    context = this,
    maxTotalFileSizeBytes = 1024 * 1024,
)
val buggyFileRollingLogger = BuggyFileRollingLogger(
    fileFactory = fileFactory::createFile,
    maxIndividualFileSizeBytes = 10 * 1024 * 1024,
)
```


`buggy-timber` module provides a Timber tree in which custom loggers can be injected. Example for
rolling file logger with Timber tree:

```kotlin
Timber.plant(
    DelegatorTimberTree(buggyFileRollingLogger::log),
)
```

## Requirements

Minimum supported Android version: API level 22 (Android 5.1)

## Contributing

We believe that the community can help us improve and build better a product.
Please refer to our [contributing guide](CONTRIBUTING.md) to learn about the types of contributions we accept and the process for submitting them.
For easier developing a [sample app](sample) with proper implementations is provided.

To ensure that our community remains respectful and professional, we defined a [code of conduct](CODE_OF_CONDUCT.md) that we expect all contributors to follow.

We appreciate your interest and look forward to your contributions.

### Publishing

When publishing a new version of the library, disable Gradle parallel execution to avoid the issue of artefacts appearing in multiple repositories
You can disable it in `gradle.properties`
Or by running command with flag, ex: `./gradlew publishReleasePublicationToSonatypeMavenCentralRepository --no-parallel`

## License

```text
Copyright 2025 Infinum

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Credits

Maintained and sponsored by [Infinum](https://infinum.com).

<div align="center">
    <a href='https://infinum.com'>
    <picture>
        <source srcset="https://assets.infinum.com/brand/logo/static/white.svg" media="(prefers-color-scheme: dark)">
        <img src="https://assets.infinum.com/brand/logo/static/default.svg">
    </picture>
    </a>
</div>
