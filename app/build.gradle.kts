import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    kotlin("plugin.serialization") version "1.9.23"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        buildConfigField(
            "String",
            "API_KEY",
            "\"${localProperties["API_KEY"]}\""
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
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}

dependencies {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}

// UI Enhancements
dependencies {
    implementation(libs.bottombar)
    implementation(libs.androidx.compose.material.icons.extended)
}