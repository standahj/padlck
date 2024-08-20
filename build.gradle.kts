import java.io.FileOutputStream
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.SpotBugsTask

plugins {
    id("java")
    id("com.github.spotbugs") version "6.0.20"
    id("pmd")
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

spotbugs {
    toolVersion = "4.8.6"
    effort.set(Effort.MAX)
    reportLevel.set(Confidence.HIGH)
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    reports {
        create("html") {
            required.set(true)
        }
        create("xml") {
            required.set(false)
        }
    }
}

pmd {
    toolVersion = "7.4.0"
    ruleSetFiles = files("config/pmd/custom-rules.xml")
    ruleSets = listOf("category/java/bestpractices.xml", "category/java/design.xml")
}

tasks.withType<Pmd> {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}
// Disable the pmdTest task as there are rules failing on use of System.out.println in he PerformanceAnalyze code
tasks.named("pmdTest").configure {
    enabled = false
}