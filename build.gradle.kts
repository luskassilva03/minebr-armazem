plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.minebr.armazem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.github.azbh111:craftbukkit-1.8.8:R")
    implementation("de.tr7zw:item-nbt-api:2.13.1")
    implementation("com.zaxxer:HikariCP:4.0.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.jetbrains:annotations:19.0.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    compileOnly(fileTree("build/resources/deps"))
    implementation("com.github.HenryFabio:inventory-api:2.0.3")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    manifest.attributes["Main-Class"] = "net.minebr.armazem.ArmazemMain"
    relocate("de.tr7zw.changeme.nbtapi", "net.minebr.armazem.nbtapi.itemnbtapi")
}
