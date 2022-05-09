plugins {
    "edu.wpi.first.GradleRIO" version "2022.4.1"
    java
    kotlin("jvm") version "1.6.0"
    'maven-publish'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "CshCyberhawks.SwoLib"
            version = "1.0.1"

            from(components["java"])
        }
    }
}

