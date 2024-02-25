plugins {
    id("com.android.application") apply false
    kotlin("android") apply false
}

val localProperties = java.util.Properties().apply {
    val file = file("local.properties")
    if (!file.exists()) file.createNewFile()
    load(file.inputStream())
}

fun localProperty(key: String): String =
    localProperties[key] as? String
        ?: error("Property $key not found in ${file("local.properties").absolutePath}")

rootProject.extra.set("localProperty", ::localProperty)

repositories {
    mavenCentral()
}
