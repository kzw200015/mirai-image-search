plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("net.mamoe.mirai-console") version "2.11.1"
}

group = "cc.jktu"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

mirai {
    jvmTarget = JavaVersion.VERSION_17
}