plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.volkov.vacancysearchapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.volkov.vacancysearchapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        viewBinding.enable = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":core"))
    implementation(project(":auth"))
    implementation(project(":listvacancy"))
    implementation(project(":cardvacancy"))
    implementation(project(":favoritevacancy"))
    implementation(project(":jobresponse"))

    implementation(libs.lifecycle.viewmodel)
    // Core Koin library
    implementation(libs.koin.core)
    // Koin AndroidX support
    implementation(libs.koin.android)
    // Koin AndroidX WorkManager support (if needed)
    implementation(libs.koin.androidx.workmanager)

    //Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.mock)
    implementation(libs.ktor.client.cio)
    implementation(libs.logback.classic)
    implementation(libs.ktor.client.content.negotiation)
    //json
    implementation(libs.ktor.serialization.kotlinx.json)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)


    //test-junit
    implementation(libs.kotlin.test.junit)

    //fragment-ktx
    implementation(libs.androidx.fragment.ktx)
    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)


}