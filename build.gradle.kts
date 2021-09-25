import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    distribution
}

group = "io.github.l0llygag.kota"
version = "0.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
    distributions {
        main {
            distributionBaseName.set("kota")
            contents {
                from("src/dist/")
                from(shadowJar)
            }
        }
    }
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "io.github.l0llygag.kota.implementations.ServerCliKt"))
        }
    }
}