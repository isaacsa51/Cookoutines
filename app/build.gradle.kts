import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("TASTY_API_KEY")

android {
    namespace = "com.serranoie.android.cookoutines"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.serranoie.android.cookoutines"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables{
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

        debug{
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "API_KEY", apiKey)
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(project(":ui-theme"))
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":feature:recipes-list"))
    implementation(project(":feature:recipes-list:domain"))
    implementation(project(":feature:onboarding"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // MockK
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.android)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Navigation component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    androidTestImplementation(libs.androidx.navigation.testing)

    // Coil
    implementation(libs.coil)
}

kapt {
    correctErrorTypes = true
}