import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "io.github.l0llygag.kota"
version = "0.1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.ajalt:clikt:2.7.1")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:1.8.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0-M1")
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks {
    build {
        dependsOn("shadowJar")
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "io.github.l0llygag.kota.implementations.ServerCliKt"))
        }
    }
}