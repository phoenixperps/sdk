import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "com.phoenixperpsfunding"
version = "1.1.0"
description = "The Phoenix Perpetuals API: REST, the account stream and market data, with retries, idempotency keys, paging and typed errors."

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// build output outside the source tree when PP_BUILD_ROOT is set (the repo may live in a synced folder)
System.getenv("PP_BUILD_ROOT")?.let { layout.buildDirectory.set(file("$it/java")) }

// Maven Central (release.yml, behind the owner's approval): signed, with sources and javadoc; the credentials and the key
// come from the release environment (ORG_GRADLE_PROJECT_mavenCentralUsername/Password, signingInMemoryKey...)
mavenPublishing {
    configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = true))
    publishToMavenCentral()
    if (providers.gradleProperty("signingInMemoryKey").isPresent) signAllPublications()
    coordinates("com.phoenixperpsfunding", "phoenixperps-sdk", version.toString())
    pom {
        name.set("Phoenix Perpetuals SDK")
        description.set(project.description)
        url.set("https://docs.phoenixperpsfunding.com")
        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("phoenixperps")
                name.set("Phoenix Perpetuals")
            }
        }
        scm {
            url.set("https://github.com/phoenixperps/sdk")
        }
    }
}

// GitHub Pages (scripts/publish-github.js): the same publication into a folder laid out as a Maven repository,
// served at https://phoenixperps.github.io/sdk/maven (publishAllPublicationsToPagesRepository -PpagesRepo=<folder>)
providers.gradleProperty("pagesRepo").orNull?.let { dir ->
    publishing {
        repositories {
            maven {
                name = "pages"
                url = uri(dir)
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.3")
}

// examples/NN-name.java: compiled with the SDK (gradle compileExamplesJava), run one with gradle -q example -Pn=01
val examples by sourceSets.creating {
    java.srcDir("examples")
    compileClasspath += sourceSets.main.get().output + sourceSets.main.get().runtimeClasspath
    runtimeClasspath += output + compileClasspath
}

tasks.register<JavaExec>("example") {
    description = "Runs one example: gradle -q example -Pn=01 [-Pargs=BTC-USD]"
    group = "application"
    classpath = examples.runtimeClasspath
    mainClass.set(providers.gradleProperty("n").map { "Example" + it.padStart(2, '0') })
    args = providers.gradleProperty("args").map { it.split(" ") }.getOrElse(emptyList())
    standardInput = System.`in`
}

tasks.check { dependsOn(tasks.named("compileExamplesJava")) }

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.jar {
    manifest {
        attributes("Implementation-Title" to "phoenixperps-sdk", "Implementation-Version" to project.version, "Automatic-Module-Name" to "com.phoenixperpsfunding.sdk")
    }
}

tasks.test {
    useJUnitPlatform()
    systemProperty("sdk.root", rootDir.parentFile.absolutePath)
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
