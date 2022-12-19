import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    `maven-publish`
}

group = "me.deotime"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.q64.io/rain-public")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // optics / algebra
    implementation("co.q64.rain:raindrop:1.20-SNAPSHOT")
}

tasks.test {
    useJUnit()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.deotime.warehouse"
            artifactId = "warehouse"
            version = "1.0.0"
            from(components["kotlin"])

            pom {
                name.set("warehouse")
                description.set("Coroutine based tool for efficient, thread safe, local storage.")
            }
        }
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}