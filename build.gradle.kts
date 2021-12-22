import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default

plugins {
  id("com.github.johnrengelman.shadow") version "7.1.1"
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("net.kyori.blossom") version "1.2.0"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "io.github.laymanuel"
version = "1.3.0-SNAPSHOT"
description = "Plugin to enable gravity/sand duping on PaperMC"

repositories {
  maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
  compileOnly("io.papermc.paper", "paper-api", "1.18.1-R0.1-SNAPSHOT")
  implementation("org.bstats", "bstats-bukkit", "2.2.1")
}

blossom {
  replaceToken("@VERSION@", project.version)
}

bukkit {
  website = "https://github.com/Laymanuel/GravityControl"
  author = "laymanuel"
  main = project.group.toString() + ".gc.GravityControl"
  apiVersion = "1.16"

  commands {
    register("gcr") {
      description = "Reload GravityControl configuration"
      aliases = listOf("gravitycontrolreload")
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
    minecraftVersion("1.18")
  }

  shadowJar {
    archiveClassifier.set(null as String?)
    relocate("org.bstats", "${project.group}.gc.lib.org.bstats")
    minimize()
  }

  build {
    dependsOn(shadowJar)
  }
}
