import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.0"
    id("org.jetbrains.changelog") version "1.1.2"
//    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
//    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.github.stleary:JSON-java:20210307")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
    version = properties("pluginVersion")
    groups = emptyList()
}

tasks {
    // Set the compatibility versions to 1.8
//    withType<JavaCompile> {
//        sourceCompatibility = "1.8"
//        targetCompatibility = "1.8"
//    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
//        pluginDescription.set(
//            File(projectDir, "README.md").readText().lines().run {
//                val start = "<!-- Plugin description -->"
//                val end = "<!-- Plugin description end -->"
//
//                if (!containsAll(listOf(start, end))) {
//                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
//                }
//                subList(indexOf(start) + 1, indexOf(end))
//            }.joinToString("\n").run { markdownToHTML(this) }
//        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(File(projectDir, "CHANGELOG.md").readText().lines().joinToString("\n").run { markdownToHTML(this) })
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}