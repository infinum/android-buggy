plugins {
    id("java-library")
    id("kotlin")
}

apply {
    from("$rootDir/config.gradle.kts")
    from("$rootDir/dokka.gradle")
    from("$rootDir/maven-publish.gradle")
    from("$rootDir/detekt.gradle")
}

val releaseConfig: Map<String, Any> by project
val sonatype: Map<String, Any> by project

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// specify per module - mostly needed due to different artifactIds, names, descriptions
extra["mavenPublishProperties"] = mapOf(
    "group" to releaseConfig["group"],
    "version" to releaseConfig["version"],
    "artifactId" to "buggyRollingFileLogger",
    "repository" to mapOf(
        "url" to sonatype["url"],
        "username" to sonatype["username"],
        "password" to sonatype["password"]
    ),
    "name" to "Buggy rolling file logger",
    "description" to "Buggy module for writing into file(s) that has limited size and rolls over",
    "url" to "https://github.com/infinum/android-buggy",
    "scm" to mapOf(
        "connection" to "https://github.com/infinum/android-buggy.git",
        "url" to "https://github.com/infinum/android-buggy"
    )
)