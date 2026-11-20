plugins {
    id("com.android.application")
}

android {
    namespace = "com.weatherland.watchface"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.weatherland.watchface"
        // WFF v2 (native Wear OS Weather data sources) requires Wear OS 5+ (API 34).
        // Supported on Galaxy Watch 4, 5, 6, 7, 8 & Ultra (One UI 6 Watch / Wear OS 5+).
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            // Configure signingConfig here before uploading the .aab to Google Play
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
