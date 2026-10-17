plugins {
    id("java")
    id("antlr")
    id("application")
    id("jacoco")
    id("org.barfuin.gradle.jacocolog") version "4.0.0"
    id("com.github.spotbugs") version "6.4.8"
    id("pmd")
    id("name.remal.sonarlint") version "7.0.1"
}

group = "fr.univ_amu.fr.formation.m1info.gla"
version = "0.1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "fr.univ_amu.fr.formation.m1info.gla.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("gg.jte:jte:3.2.1")
    implementation("org.apache.commons:commons-collections4:4.5.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.20.1")
    implementation("org.hsqldb:hsqldb:2.7.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("commons-io:commons-io:2.21.0")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    antlr("org.antlr:antlr4:4.13.2")
    testImplementation("org.assertj:assertj-core:3.27.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.21.0")
    testImplementation("org.mockito:mockito-core:5.21.0")
    implementation("com.google.inject:guice:7.0.0")
    implementation("com.google.guava:guava:33.5.0-jre")
}
spotbugs {
    toolVersion = "4.9.8"
}
pmd {
    isConsoleOutput = true
    toolVersion = "7.16.0"
    rulesMinimumPriority = 5
    ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    jvmArgs = listOf(
        "-XX:+EnableDynamicAgentLoading", "-Xshare:off"
    )
}
tasks.jacocoTestReport {
    dependsOn("test")
    reports {
        xml.required = true
        html.required = true
    }
}