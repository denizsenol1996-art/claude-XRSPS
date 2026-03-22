plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.0")
    }
}


allprojects {
    apply(plugin = "java")
    apply(plugin = "application")
    apply(plugin = "com.github.johnrengelman.shadow")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}

dependencies {
    implementation(project("game"))
}

tasks.withType<JavaCompile>().configureEach {
    options.isWarnings = false
    options.isDeprecation = false
    options.isIncremental = true
}

tasks {
    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\"))
    }
}

application {
    mainClass.set("Application")
}

tasks {
    val gameProjectPath = "game"
    val runeliteProjectPath = "runelite"

    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\tmp\\"))
    }

}


allprojects { tasks.withType<JavaCompile> { options.forkOptions.memoryMaximumSize = "4g" } }
