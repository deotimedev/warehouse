import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
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
    implementation("co.q64.rain:raindrop:1.19-SNAPSHOT")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}