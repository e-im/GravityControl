import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default

plugins {
  id("com.github.johnrengelman.shadow") version "7.1.0"
  id("xyz.jpenilla.run-paper") version "1.0.5"
  id("net.kyori.blossom") version "1.2.0"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "io.github.laymanuel"
version = "1.1"
description = "Plugin to enable gravity/sand duping on PaperMC"

repositories {
  maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
  compileOnly("com.destroystokyo.paper", "paper-api", "1.13.2-R0.1-SNAPSHOT")
  implementation("org.bstats", "bstats-bukkit", "2.2.1")
}

blossom {
  replaceToken("@VERSION@", project.version)
}

bukkit {
  website = "https://github.com/Laymanuel/GravityControl"
  author = "Laymanuel"
  main = project.group.toString() + ".gc.GravityControl"
  apiVersion = "1.13"

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
  compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
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
