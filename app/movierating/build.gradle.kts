import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.mudrichenkoevgeny.movierating"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mudrichenkoevgeny.movierating"

        minSdk = libs.versions.minSdk.get().toInt()

        targetSdk = libs.versions.targetSdk.get().toInt()

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = libs.versions.androidTestJUnitRunner.get()
    }

    val keystorePropertiesFile = rootProject.file("app/movierating/signing/mudrichenkoevgenykeystore.properties")
    val keystoreProperties = Properties()

    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(keystorePropertiesFile.inputStream())
    } else {
        logger.warn("Keystore properties file not found! Using default values.")
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"]?.toString() ?: "storeFile")
            storePassword = keystoreProperties["storePassword"]?.toString() ?: "storePassword"
            keyAlias = keystoreProperties["keyAlias"]?.toString() ?: "keyAlias"
            keyPassword = keystoreProperties["keyPassword"]?.toString() ?: "keyPassword"
        }
    }

    buildTypes {
        debug {
            val apiBaseUrl = project.findProperty("BASE_API_URL_DEV")
                ?: error("Missing BASE_API_URL_DEV in gradle.properties")
            buildConfigField("String", "BASE_API_URL", "$apiBaseUrl")
            val isMockRestApi = true
            buildConfigField("Boolean", "IS_MOCK_REST_API", "$isMockRestApi")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")

            val apiBaseUrl = project.findProperty("BASE_API_URL_PROD")
                ?: error("Missing BASE_API_URL_PROD in gradle.properties")
            buildConfigField("String", "BASE_API_URL", "$apiBaseUrl")
            val isMockRestApi = false
            buildConfigField("Boolean", "IS_MOCK_REST_API", "$isMockRestApi")
        }
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // modules
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":feature:user"))
    implementation(project(":feature:settings"))

    // compilers
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)

    // libs
    implementation(libs.hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.material)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
