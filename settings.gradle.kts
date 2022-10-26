pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
    }
}

plugins {
    id("com.gradle.enterprise") version "3.11.2"
}

gradleEnterprise {
    server = "https://e.grdev.net"
    buildScan {
        publishAlways()
    }
}
rootProject.name = "keycloak-custom-server"