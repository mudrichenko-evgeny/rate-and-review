plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mudrichenkoevgeny.core.common"

    compileSdk = libs.versions.compileSdk.get().toInt()

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // libs
    implementation(libs.hilt)

    // test
    testImplementation(libs.junit)
}