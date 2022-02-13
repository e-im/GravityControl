import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default

plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("java")
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "dev.thoughtcrime"
version = "1.3.0"
description = "Plugin to enable gravity/sand duping on PaperMC"

repositories {
  maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
  compileOnly("io.papermc.paper", "paper-api", "1.18.1-R0.1-SNAPSHOT")
  implementation("org.bstats", "bstats-bukkit", "2.2.1")
}

bukkit {
  website = "https://github.com/Laymanuel/GravityControl"
  author = "laymanuel"
  main = "${project.group}.${project.name.toLowerCase()}.${project.name}"
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

  jar {
    archiveClassifier.set("unshaded")
  }

  runServer {
    minecraftVersion("1.18")
  }

  shadowJar {
    archiveClassifier.set(null as String?)
    relocate("org.bstats", "${project.group}.${project.name.toLowerCase()}.libs.bstats")
    minimize()
  }

  assemble {
    dependsOn(shadowJar)
  }
}
