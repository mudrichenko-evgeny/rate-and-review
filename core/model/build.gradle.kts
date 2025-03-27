plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mudrichenkoevgeny.core.model"

    compileSdk = libs.versions.compileSdk.get().toInt()

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // modules
    implementation(project(":core:common"))

    // libs
    implementation(libs.kotlinx.serialization.json)
}