plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.volkov.favoritevacancy"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        viewBinding.enable = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.lifecycle.viewmodel)

    implementation(project(":core"))
    implementation(project(":cardvacancy"))
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

    //coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)
    // implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.kotlinx.coroutines.test)

    //fragment-ktx
    implementation(libs.androidx.fragment.ktx)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Unit test
    //androidTestImplementation(libs.mockk)

    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlin.test.junit)
    implementation(libs.truth.truth)

    // Зависимость для работы с Ktor (если используете Ktor)
    androidTestImplementation(libs.ktor.client.android)
    // androidTestImplementation(libs.ktor.client.mock)
//    androidTestImplementation(libs.ktor.client.core)
//    androidTestImplementation(libs.ktor.client.serialization)
//    androidTestImplementation(libs.ktor.client.logging)
//    androidTestImplementation(libs.ktor.client.cio)
//    androidTestImplementation(libs.ktor.client.content.negotiation)
    // Koin для тестов
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.koin.test)
    // Для мокирования зависимостей
    testImplementation(libs.koin.test.junit)
//    androidTestImplementation(libs.koin.test.junit)
    //mokito для тестов
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.mockk)
    testImplementation(libs.app.cash.turbine)
    //implementation(libs.mockk)

    //androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.test.runner) // AndroidJUnitRunner
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.ktor.client.mock)

}