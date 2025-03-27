plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mudrichenkoevgeny.core.storage"

    compileSdk = libs.versions.compileSdk.get().toInt()

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // modules
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // compilers
    ksp(libs.hilt.compiler)

    // libs
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.datastore)
    implementation(libs.hilt)
}