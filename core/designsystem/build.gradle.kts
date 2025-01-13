plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "ru.glebik.core.designsystem"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompiler.get()
    }
}

dependencies {
    api(libs.androidx.core.ktx)

    api(libs.material)

    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}