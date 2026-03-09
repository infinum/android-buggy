plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "Android Buggy"

include(":sample")

include(":buggy")
include(":buggy-timber-logger")
include(":buggy-rolling-file-logger")
include(":buggy-android")
