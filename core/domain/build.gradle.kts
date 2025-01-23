plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    //RX Java
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    //implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
}