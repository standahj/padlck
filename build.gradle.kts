import java.io.FileOutputStream

plugins {
    id("java")
}

group = "com.cleverthis.interview"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":padlock-impl"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    // if you ever need import more dependencies, following this format:
    // implementation("group-id:project-id:version")
    // just like the logback classic
    // implementation("ch.qos.logback:logback-classic:1.5.3")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-Dfast=true")
}

tasks.register<JavaExec>("runPerformanceAnalyze")
tasks.named<JavaExec>("runPerformanceAnalyze") {
    dependsOn("testClasses")
    group = "verification"
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("com.cleverthis.interview.PerformanceAnalyze")
    jvmArgs("-Dfast=true")
    standardOutput = FileOutputStream("performance.txt")
}