import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default

plugins {
  id("java")
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "one.eim"
version = "1.4.0-SNAPSHOT"
description = "Plugin to enable gravity/sand duping on PaperMC"

repositories {
  maven("https://papermc.io/repo/repository/maven-public/") {
    name = "PaperMC"
  }
}

dependencies {
  compileOnly("io.papermc.paper", "paper-api", "1.18.2-R0.1-SNAPSHOT")
  implementation("org.bstats", "bstats-bukkit", "3.0.0")
}

bukkit {
  website = "https://github.com/sulu5890/GravityControl"
  authors = listOf("laymanuel", "sulu")
  main = "one.eim.gravitycontrol.GravityControl"
  apiVersion = "1.16"

  commands {
    register("gcr") {
      description = "Reload GravityControl configuration"
      permission = "gravitycontrol.reload"
      usage = "/gcr"
    }
  }

  permissions {
    register("gravitycontrol.reload") {
      description = "Allows use of the /gcr command"
      default = Default.OP
    }
  }
}

tasks {
  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }

  runServer {
    minecraftVersion("1.18.2")
  }

  shadowJar {
    archiveClassifier.set(null as String?)
    relocate("org.bstats", "one.eim.gravitycontrol.libs.bstats")
  }

  assemble {
    dependsOn(shadowJar)
  }
}
