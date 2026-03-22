group = "net.cryptic"
version = "0.0.1"

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm")
}

tasks.withType<Jar> {
    manifest {
        application {
            mainClass.set("Application")
            applicationDefaultJvmArgs = listOf()
        }
        attributes("Main-Class" to "net.runelite.client.Application")
    }
}

dependencies {
    implementation(project(":runelite"))
    implementation(group = "com.github.akman", name = "jpackage-maven-plugin", version = "0.1.5")

    implementation(files("../libs/java-discord-rpc-2.0.1-all.jar"))
    implementation(files("../libs/SwiftFUP-client-3.6.1.jar"))
    implementation("com.displee:rs-cache-library:7.1.1")
    implementation(group = "com.google.inject", name = "guice", version = "5.0.1")
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.lang3)

    implementation(libs.netty)
    implementation(libs.fastutil)

    implementation(libs.slf4j.api)
    runtimeOnly(libs.slf4j.simple)


    val lombok = module("org.projectlombok", "lombok", "1.18.26")
    compileOnly(lombok)
    annotationProcessor(lombok)
    testCompileOnly(lombok)
    testAnnotationProcessor(lombok)
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    register<JavaExec>("Run-Normal") {
        group = "Runelite"
        description = "Run Runelite in Normal Mode"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

    register<JavaExec>("Run-Development") {
        group = "Runelite"
        description = "Run Runelite in Development Mode"
        enableAssertions = true
        args = listOf("--developer-mode")
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\libs\\"))
        archiveBaseName.set(project.name)
        from(sourceSets.main.get().resources) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }

}

application {
    mainClass.set("Application")
}
