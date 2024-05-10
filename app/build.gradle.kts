plugins {
    id("com.android.application")
    kotlin("android")
}

val localProperty: (String) -> String by rootProject.extra

val androidxCoreVersion: String by project
val androidxActivityVersion: String by project
val androidxNavigationVersion: String by project
val composeBomVersion: String by project
val composeCompilerVersion: String by project

android {
    namespace = "io.github.d4isdavid.educhat"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.d4isdavid.educhat"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"${localProperty("api.baseUrl")}\"")
        }

        release {
            buildConfigField("String", "API_BASE_URL", "\"${localProperty("api.baseUrl")}\"")

            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))

    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.activity:activity-compose:$androidxActivityVersion")
    implementation("androidx.navigation:navigation-compose:$androidxNavigationVersion")

    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
