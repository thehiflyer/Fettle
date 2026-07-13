import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.plugins.signing.Sign
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    `java-library`
    jacoco
    checkstyle
    `maven-publish`
    signing
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

group = "se.fearless"
base {
    archivesName.set("fettle")
}
version = "2.0.0"

defaultTasks("build")

val disableJavadoc = providers.gradleProperty("disableJavadoc").map(String::toBoolean).getOrElse(false)

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.slf4j:slf4j-api:2.0.16")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.5.13")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register<Jar>("gwtJar") {
    dependsOn(tasks.classes)
    archiveClassifier.set("gwt")
    from(sourceSets.main.get().output.classesDirs)
    exclude("**/export/DotExporter*", "**/util/PredicateCondition*")
    into("se/fearless/fettle/super") {
        from(sourceSets.main.get().allJava)
    }
    into("se/fearless/fettle") {
        from(sourceSets.main.get().resources)
        exclude("**/export*")
    }
}

tasks.named<Jar>("jar") {
    exclude("**/*.xml")
}

tasks.named("checkstyleMain") {
    doLast {
        val reportsDir = layout.buildDirectory.dir("reports/checkstyle").get().asFile
        val xmlReport = File(reportsDir, "main.xml")
        if (xmlReport.exists()) {
            ant.withGroovyBuilder {
                "xslt"(
                    "in" to xmlReport,
                    "style" to "config/checkstyle/checkstyle.xsl",
                    "out" to File(reportsDir, "checkstyle.html").absolutePath,
                )
            }
        }
    }
}

tasks.named<Checkstyle>("checkstyleTest") {
    isIgnoreFailures = true
}

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        html.required.set(true)
        csv.required.set(false)
        xml.required.set(false)
    }
    sourceDirectories.setFrom(files(sourceSets.main.get().allJava.srcDirs))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    // Mockito's ByteBuddy and CompleteCoverageTest's setAccessible() need this on Java 21+.
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

tasks.withType<Javadoc>().configureEach {
    isEnabled = !disableJavadoc
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.named<Jar>("javadocJar") {
    isEnabled = !disableJavadoc
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named("gwtJar"))

            pom {
                name.set("Fettle")
                description.set("Fettle is a state machine framework for java")
                url.set("https://thehiflyer.github.com/Fettle/")

                scm {
                    url.set("https://github.com/thehiflyer/Fettle.git")
                    connection.set("scm:git:https://github.com/thehiflyer/Fettle.git")
                    developerConnection.set("scm:git:https://github.com/thehiflyer/Fettle.git")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("hiflyer")
                        name.set("Per Malmen")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            val releases = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshots = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshots else releases
            credentials {
                username = findProperty("sonatypeUsername") as String? ?: ""
                password = findProperty("sonatypePassword") as String? ?: ""
            }
        }
    }
}

signing {
    isRequired = !version.toString().endsWith("SNAPSHOT")
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign>().configureEach {
    onlyIf("Only sign when publishing a release") {
        !version.toString().endsWith("SNAPSHOT") &&
            gradle.taskGraph.allTasks.any { it.name.startsWith("publish") }
    }
}

artifacts {
    add("default", tasks.named("gwtJar"))
}
