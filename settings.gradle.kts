pluginManagement {
    val kotlinVersion: String by settings
    val androidPluginVersion: String by settings

    repositories {
        google()
        mavenCentral()
    }

    plugins {
        kotlin("android") version kotlinVersion apply false
        id("com.android.application") version androidPluginVersion apply false
    }
}

rootProject.name = "EduChat-app"
include(":app")
