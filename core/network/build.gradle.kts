plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "ru.glebik.core.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    api(libs.bundles.network)
    api(libs.kotlinx.serialization.json)

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)

    testImplementation(libs.junit)
}