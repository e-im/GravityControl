plugins {
    id("java")
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper", "paper-api", "1.13.2-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}