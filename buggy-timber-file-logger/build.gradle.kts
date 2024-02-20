plugins {
    id("com.android.library")
    kotlin("android")
}

apply {
    from("$rootDir/config.gradle.kts")
    from("$rootDir/dokka.gradle")
    from("$rootDir/maven-publish.gradle")
    from("$rootDir/detekt.gradle")
}

val releaseConfig: Map<String, Any> by project
val sonatype: Map<String, Any> by project
val buildConfig: Map<String, Any> by project

android {
    compileSdk = buildConfig["compileSdk"] as Int

    defaultConfig {
        minSdk = buildConfig["minSdk"] as Int
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    namespace = "com.infinum.buggy"

    kotlin {
        jvmToolchain(8)
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}


extra["mavenPublishProperties"] = mapOf(
    "group" to releaseConfig["group"],
    "version" to releaseConfig["version"],
    "artifactId" to "buggyTimberFile",
    "repository" to mapOf(
        "url" to sonatype["url"],
        "username" to sonatype["username"],
        "password" to sonatype["password"]
    ),
    "name" to "Buggy Timber file",
    "description" to "Buggy module for writing into file with Timber",
    "url" to "https://github.com/infinum/android-buggy",
    "scm" to mapOf(
        "connection" to "https://github.com/infinum/android-buggy.git",
        "url" to "https://github.com/infinum/android-buggy"
    )
)