import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    kotlin("plugin.serialization") version "1.9.23"
    alias(libs.plugins.dagger.hilt)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.msayeh.breeze"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.msayeh.breeze"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        buildConfigField(
            "String",
            "API_KEY",
            "\"${localProperties["API_KEY"]}\""
        )
        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"${localProperties["MAPS_API_KEY"]}\""
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Instrumented Testing
dependencies {
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}

// Unit Testing
dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hamcrest)
    testImplementation(libs.hamcrest.library)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
}

// Networking
dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
}

// Local Database
dependencies {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}

// Coroutines
dependencies {
    implementation(libs.kotlinx.coroutines.android)
}

// Coil
dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}

// Navigation
dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}

// UI Enhancements
dependencies {
    implementation(libs.bottombar)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.lottie)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.material3)
}

// Dependency Injection
dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}

// Backward Compatibility
dependencies {
    coreLibraryDesugaring(libs.desugaring)
}

// Play Services Location
dependencies {
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.maps.compose)
}

// Data Store
dependencies {
    implementation(libs.androidx.datastore.preferences)
}