import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
  maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
  compileOnly("com.destroystokyo.paper", "paper-api", "1.13.2-R0.1-SNAPSHOT")
  implementation("org.bstats", "bstats-bukkit", "2.2.1")
}

tasks.create<ConfigureShadowRelocation>("relocateShadowJar") {
  target = tasks["shadowJar"] as ShadowJar
  prefix = "io.github.laymanuel.gc.libs"
}

tasks.named<ShadowJar>("shadowJar").configure {
  dependsOn(tasks["relocateShadowJar"])
  minimize()
}


tasks {
  compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
  }
}
