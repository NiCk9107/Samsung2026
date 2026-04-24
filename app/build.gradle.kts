plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

application {
    mainClass.set("ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:3.0.1")
    implementation("io.ktor:ktor-server-cio-jvm:3.0.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.1")
    implementation("io.ktor:ktor-server-status-pages-jvm:3.0.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("org.jetbrains.exposed:exposed-core:0.47.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.47.0")
    implementation("org.postgresql:postgresql:42.7.3")


    testImplementation("io.ktor:ktor-server-test-host-jvm:3.0.1")
    testImplementation(kotlin("test"))
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}