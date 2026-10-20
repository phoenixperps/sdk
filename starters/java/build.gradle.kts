// The starter bot (../README.md): gradle -q run. It uses the published SDK; --include-build <the SDK's java folder> runs
// it against a local one instead (the conformance run).
plugins {
    application
}

repositories {
    mavenCentral()
    maven("https://phoenixperps.github.io/sdk/maven")
}

dependencies {
    implementation("com.phoenixperpsfunding:phoenixperps-sdk:1.0.0")
}

application {
    mainClass.set("Starter")
}

tasks.withType<JavaCompile> {
    options.release.set(17)
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
}

tasks.named<JavaExec>("run") {
    workingDir = projectDir
    standardInput = System.`in`
}
