@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.nameisjayant.gradle"
    compileSdk = 33

    signingConfigs {
        getByName("debug") {
            storeFile = file("/Users/jayantkumar/AndroidStudioProjects/gradle/app/keystore.jks")
            keyAlias = "gradle"
            keyPassword = "123456"
            storePassword = "123456"
        }
        create("release") {
            storeFile = file("/Users/jayantkumar/AndroidStudioProjects/gradle/app/keystore.jks")
            keyAlias = "gradle"
            keyPassword = "123456"
            storePassword = "123456"
        }
        create("staging") {
            storeFile = file("/Users/jayantkumar/AndroidStudioProjects/gradle/app/keystore.jks")
            keyAlias = "gradle"
            keyPassword = "123456"
            storePassword = "123456"
        }
    }

    defaultConfig {
        applicationId = "com.nameisjayant.gradle"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs["debug"]
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        create("staging") {
            signingConfig = signingConfigs["staging"]
            // copy configuration from .debug build type
            initWith(getByName("debug"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
    }

    flavorDimensions += listOf("mode")

    productFlavors {
        create("free") {
            dimension = "mode"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
        }
        create("paid") {
            dimension = "mode"
            applicationIdSuffix = ".paid"
            versionNameSuffix = "-paid"
        }

//        create("minApi21"){
//            dimension = "api"
//            minSdk = 21
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}