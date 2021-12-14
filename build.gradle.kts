@file:Suppress("GradlePackageUpdate", "DEPRECATION")

plugins {
    kotlin("js") version "1.5.30"
}

//gradle/webpack issue fix; not needed in some cases.
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class.java) {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().versions.webpackCli.version = "4.9.0"
}

group = "me.sadmin"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

fun getName(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"

val kotlinWrappersVersion = "0.0.1-pre.242-kotlin-1.5.30"

dependencies {
    testImplementation(kotlin("test", "1.5.30"))
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.2")
    implementation(enforcedPlatform(getName("wrappers-bom:${kotlinWrappersVersion}")))
    implementation(getName("react"))
    implementation(getName("react-dom"))
    implementation(getName("styled"))
    implementation(getName("react-router-dom"))
    //table wrapper:
    implementation(getName("react-table"))
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}