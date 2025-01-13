plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.glebik.tinkoff_fintech"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.glebik.tinkoff_fintech"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "ru.glebik.tinkoff_fintech.util.TestAppRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
        animationsDisabled = true
    }
}

dependencies {

    implementation(project(":core:db"))
    implementation(project(":core:utils"))
    implementation(project(":core:presentation"))
    implementation(project(":core:network"))

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)

    implementation(libs.kotlinx.immutable)

    // Fragment Testing
    implementation(libs.androidx.fragment.testing)
    // Andriod Test Rules
    implementation(libs.androidx.rules)
    // Compose Test
    androidTestImplementation(libs.androidx.compose.test.junit4)
    debugImplementation(libs.androidx.compose.test.manifest)
    // JUnit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    // Mockk
    testImplementation(libs.mockk)
    // Kotest
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    // Hamcrest Matchers
    androidTestImplementation(libs.hamcrest)
    // Kaspresso
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.compose)
    // Espresso Intents
    androidTestImplementation(libs.androidx.espresso.intents)
    // Wiremock
    debugImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

}