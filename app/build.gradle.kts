import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

val keyStorePropertyFile = rootProject.file("gradle.properties")
val keyStoreProperty = Properties()
keyStoreProperty.load(FileInputStream(keyStorePropertyFile))

android {
    namespace = "com.nameisjayant.gradle"
    compileSdk = 33

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


    buildFeatures {
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("/src/main/java")
            res.srcDirs("src/main/res")
        }
        getByName("debug") {
            kotlin.srcDirs("/src/main/kotlin")
            res.srcDirs("src/main/res")
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = keyStoreProperty["keyAlias"] as String
            storeFile = file(keyStoreProperty["keyStore"] as String)
            keyPassword = keyStoreProperty["keyPassword"] as String
            storePassword = keyStoreProperty["storePassword"] as String
        }
        create("release") {
            keyAlias = keyStoreProperty["keyAlias"] as String
            storeFile = file(keyStoreProperty["keyStore"] as String)
            keyPassword = keyStoreProperty["keyPassword"] as String
            storePassword = keyStoreProperty["storePassword"] as String
        }
        create("staging") {
            keyAlias = keyStoreProperty["keyAlias"] as String
            storeFile = file(keyStoreProperty["keyStore"] as String)
            keyPassword = keyStoreProperty["keyPassword"] as String
            storePassword = keyStoreProperty["storePassword"] as String
        }
    }


    buildTypes {
        release {
            buildConfigField("String", "BASE_URL", "\"https://www.prod.com/\"")
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"https://www.dev.com/\"")
            signingConfig = signingConfigs["debug"]
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

        }
        create("staging") {
            buildConfigField("String", "BASE_URL", "\"https://www.staging.com/\"")
            signingConfig = signingConfigs["staging"]
            // copied configuration from .debug build type
            initWith(getByName("debug"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
            resValue("string", "flavour_name", "\"free\"")
            //  applicationId = "com.nameisjayant.gradle"
        }
        create("paid") {
            dimension = "version"
            applicationIdSuffix = ".paid"
            versionNameSuffix = "-paid"
            resValue("string", "flavour_name", "\"paid]\"")
            // applicationId = "com.nameisjayant.gradle"
        }
    }

    applicationVariants.forEach { variant ->
        if (variant.buildType.name == "release") {
            variant.outputs.forEach { output ->
                val fileName = output.outputFile.name
                if (fileName.endsWith(".apk")) {
                    val apkName = "${variant.productFlavors[0].name}-v${variant.versionName}.apk"
                    output.outputFile.renameTo(File(output.outputFile.parent, apkName))
                }
            }
        }
    }

//    flavorDimensions += listOf("version", "env")
//
//    productFlavors {
//        create("free") {
//            dimension = "version"
//            applicationIdSuffix = ".free"
//            versionNameSuffix = "-free"
//        }
//        create("paid") {
//            dimension = "version"
//            applicationIdSuffix = ".paid"
//            versionNameSuffix = "-paid"
//        }
//
//        create("dev") {
//            dimension = "env"
//        }
//        create("prod") {
//            dimension = "env"
//        }
//    }

    androidComponents {
        beforeVariants { builder ->
            if (builder.productFlavors.containsAll(listOf("version" to "free", "env" to "dev"))) {
                builder.enable = false
            }
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
    //implementation(project(":domain"))
    // "freeImplementation"(project(":domain"))
}