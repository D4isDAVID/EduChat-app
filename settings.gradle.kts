pluginManagement {
    val localProperties = java.util.Properties().apply {
        val file = file("local.properties")
        if (!file.exists()) file.createNewFile()
        load(file.inputStream())
    }

    fun localProperty(key: String): String =
        localProperties[key] as? String
            ?: error("Property $key not found in ${file("local.properties").absolutePath}")

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
