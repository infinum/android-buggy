// Maven central / CI build badges / links (Github pages, wiki, etc.) //

### <img align="left" src="logo.svg" width="48">

# Android Buggy

// Library logo (left wrap with title) //

Android library for a simple resource (usually logs and debug data) exporting library that allows
you to add, remove and process resources before the export.

The project is organized in the following modules:

- `buggy` - Core module for adding, processing and exporting resources (logs and debug data in most
  cases).
- `buggy-android` - Module for providing Android specific details about device and app
  as `BuggyResource`.
- `buggy-rolling-file-logger` - Module for writing logs to files with a rolling strategy (once
  maximum total capacity is exceeded oldest files get deleted).
- `buggy-timber` - Module that provides a Timber tree in which custom loggers can be injected.

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

### Usage

To use the library, you have to initialize it with Builder. You can add resources (for example log
files) and processors (for example `EncryptionBuggyResourceProcessor`) to the builder and then build
it.
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

To use the `Buggy` instance, you can call the `export` method with a `Exporter` instance. Then
library will export all resources processed by defined processors with exporter defined strategy.
Example of exporting resources to a zip file:

```kotlin
buggy.export(
    ZipBuggyExporter(
        file = report,
    ),
)
```

Additional modules provide additional functionality. For example, `buggy-android` module provides
`BuggyResource` implementations for Android specific details about device and app.

```kotlin
Buggy.Builder()
    .add(ApplicationInfoBuggyResource(context))
    // ... other resources and processors
    .build()
```

`buggy-rolling-file-logger` module provides a `RollingFileLogger` which can be used to write logs to
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

`buggy-timber` module provides a Timber tree in which custom loggers can be injected.

```kotlin
Timber.plant(
    DelegatorTimberTree(buggyFileRollingLogger::log),
)
```

Check the [sample app](sample) for more detailed examples.

### // OPTIONAL: Configuration //

// How to further configure the library, list any additional options //

### // OPTIONAL: Specific usage info //

// Depending on the complexity of the library, you might have one or more sections where you explain
specific use-cases which do not fall under "basic" usage. You **should not** cover the whole API
here, just some of the more common usages of the library. //

## Requirements

Minimum supported Android version: API level 28 (Android 9.0)

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short
description of your changes. By making contributions to this project you give permission for your
code to be used under the same [license](LICENSE).

// Add any other info specific to contributing to this library (e.g., if you have a sample app to
help with development) //

## License

```
Copyright 2021 Infinum

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

Maintained and sponsored by [Infinum](http://www.infinum.com).

<p align="center">
  <a href='https://infinum.com'>
    <picture>
        <source srcset="https://assets.infinum.com/brand/logo/static/white.svg" media="(prefers-color-scheme: dark)">
        <img src="https://assets.infinum.com/brand/logo/static/default.svg">
    </picture>
  </a>
</p>
