plugins {
    java
    id("io.quarkus") version "2.7.5.Final"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repository.jboss.org/nexus/content/repositories/public")
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

val keycloakVersion = "18.0.2"

val keycloakDist by configurations.creating

dependencies {
    keycloakDist("org.keycloak:keycloak-quarkus-dist:$keycloakVersion@zip")
    implementation("org.keycloak:keycloak-quarkus-server:$keycloakVersion") {
        exclude("org.wildfly.security", "wildfly-elytron")

        exclude("mysql", "mysql-connector-java")
        exclude("io.quarkus", "quarkus-jdbc-mysql")
        exclude("io.quarkus", "quarkus-jdbc-mysql-deployment")

        exclude("com.microsoft.sqlserver", "mssql-jdbc")
        exclude("io.quarkus", "quarkus-jdbc-mssql")
        exclude("io.quarkus", "quarkus-jdbc-mssql-deployment")

        exclude("com.oracle.database.jdbc", "ojdbc11")
        exclude("io.quarkus", "quarkus-jdbc-oracle")
        exclude("io.quarkus", "quarkus-jdbc-oracle-deployment")

        exclude("org.mariadb.jdbc", "mariadb-java-client")
        exclude("io.quarkus", "quarkus-jdbc-mariadb")
        exclude("io.quarkus", "quarkus-jdbc-mariadb-deployment")

        exclude("com.h2database", "h2")
        exclude("io.quarkus", "quarkus-jdbc-h2")
        exclude("io.quarkus", "quarkus-jdbc-h2-deployment")
    }
    implementation(platform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    constraints {
        implementation("org.keycloak:keycloak-core:$keycloakVersion")
        implementation("org.jboss.resteasy:resteasy-client:4.7.5.Final")
        implementation("com.thoughtworks.xstream:xstream:1.4.19")
        implementation("org.postgresql:postgresql:42.3.5")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
        implementation("org.apache.commons:commons-compress:1.21")
    }
}

group = "com.gradle"
//version = "18.0.2-GE"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

ext {
    set("quarkus.package.type", "mutable-jar")
}

quarkus {
    setFinalName("quarkus-run.jar")
}

tasks {
    register<Copy>("aggregateCustomKeycloakServer") {
        from(provider { zipTree(keycloakDist.singleFile) }) {
            exclude("**/lib/**")
        }
        from(quarkusBuild.map { it.fastJar }) {
            into("keycloak-$keycloakVersion/lib")
        }

        into("$buildDir")
    }
}
