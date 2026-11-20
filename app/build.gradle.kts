import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
}

val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties()
if (keystorePropsFile.exists()) {
    keystoreProps.load(FileInputStream(keystorePropsFile))
}

android {
    namespace = "com.soycamiloypunto.weatherland"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.soycamiloypunto.weatherland"
        // WFF v2 (native Wear OS Weather data sources) requires Wear OS 5+ (API 34).
        // Supported on Galaxy Watch 4, 5, 6, 7, 8 & Ultra (One UI 6 Watch / Wear OS 5+).
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1"
    }

    buildFeatures {
        buildConfig = false
    }

    signingConfigs {
        create("release") {
            if (keystorePropsFile.exists()) {
                storeFile = rootProject.file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            // Required for Watch Face Format: R8 strips unused R.class so the AAB has ZERO .dex files
            isMinifyEnabled = true
            isShrinkResources = false
            if (keystorePropsFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    // Watch Face Format bundles contain only resources (no Kotlin/Java code).
    // Keep assets uncompressed where needed and generate a clean standalone AAB.
    bundle {
        abi {
            enableSplit = false
        }
        density {
            enableSplit = false
        }
        language {
            enableSplit = false
        }
    }
}
