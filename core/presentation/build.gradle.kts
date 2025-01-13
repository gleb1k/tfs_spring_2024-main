plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "ru.glebik.core.presentation"
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
    api(project(":core:widget"))
    api(project(":core:navigation"))

    api(libs.bundles.mviKotlin)

    api(libs.androidx.appcompat)
    api(libs.androidx.fragment.ktx)

    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}