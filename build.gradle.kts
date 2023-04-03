import java.io.FileOutputStream
import java.nio.file.Files.createTempFile

plugins {
  id("java")
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "one.eim"
version = "2.0.0"
description = "Plugin to enable gravity/sand duping on PaperMC"

repositories {
  maven("https://papermc.io/repo/repository/maven-public/") {
    name = "PaperMC"
  }
  maven("https://maven.enginehub.org/repo/") {
    name = "EngineHub"
  }
}

dependencies {
  compileOnly("dev.folia", "folia-api", "1.19.4-R0.1-SNAPSHOT")
  compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.0.7")
  implementation("org.bstats", "bstats-bukkit", "3.0.0")
}


fun getJar(url: String, name: String): File {
  val file = createTempFile(name, ".jar").toFile();
  uri(url).toURL().openStream().use { it.copyTo(FileOutputStream(file)) }

  return file
}

tasks {
  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }

  runServer {
    minecraftVersion("1.18.2")

    val worldEditUrl = "https://mediafiles.forgecdn.net/files/3697/296/worldedit-bukkit-7.2.10.jar"
    val worldGuardUrl = "https://mediafiles.forgecdn.net/files/3677/516/worldguard-bukkit-7.0.7-dist.jar"
    pluginJars(getJar(worldEditUrl, "WorldEdit"), getJar(worldGuardUrl, "WorldGuard"))
  }

  shadowJar {
    archiveClassifier.set(null as String?)
    relocate("org.bstats", "one.eim.gravitycontrol.libs.bstats")
  }

  assemble {
    dependsOn(shadowJar)
  }
}
