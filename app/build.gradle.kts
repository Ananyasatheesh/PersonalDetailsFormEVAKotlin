plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.personaldetailsform_kotlin"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.personaldetailsform_kotlin"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {

        create("release") {

            storeFile = rootProject.file("release-key.jks")
            storePassword = "@Satheesh190504"
            keyAlias = "personaldetailskey"
            keyPassword = "@Satheesh190504"
        }
    }

    // How an application must run. With what configurations must an app run
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // if true, proGuard is run
            // Removes unwanted classes, methods, files from code
            isMinifyEnabled = true
            // Removes unwanted resources - drawables, strings, layouts
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += listOf(
        "edition",
        "environment"
    )

    // flavors - diff versions of an app (paid, free etc.,)
    // Here - preview, with media (upload img)
    productFlavors {
        create("preview"){
            dimension = "edition"
            applicationIdSuffix = ".preview"
            buildConfigField("Boolean", "ENABLE_IMAGE_UPLOAD", "false")
        }
        create("media"){
            dimension = "edition"
            applicationIdSuffix = ".media"
            buildConfigField("Boolean", "ENABLE_IMAGE_UPLOAD", "true")
        }
        create("dev"){
            dimension = "environment"
            applicationIdSuffix = ".dev"
            buildConfigField("Boolean", "USE_MOCK_DATA", "true")
            buildConfigField("String", "MOCK_DATA_SET", "\"DEV\"")
        }
        create("qa"){
            dimension = "environment"
            applicationIdSuffix = ".qa"
            buildConfigField("Boolean", "USE_MOCK_DATA", "true")
            buildConfigField("String", "MOCK_DATA_SET", "\"QA\"")
        }
        create("prod"){
            dimension = "environment"
            applicationIdSuffix = ".prod"
            buildConfigField("Boolean", "USE_MOCK_DATA", "false")
            buildConfigField("String", "MOCK_DATA_SET", "\"NONE\"")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    // Retrofit
    implementation(libs.retrofit)
    // Retrofit with Scalar Converter - Converts JSON to String
    implementation(libs.retrofit.scalars)
    // Retrofit with Gson Converter - Converts JSON to GSON
    implementation(libs.converter.gson)
    // Glide -> Loads image with URL
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

// APK vs AAB
// APK - Android package - contains all files, can install directly in device
// AAB - Android App Bundle - can only be published using Google Play Store,
//       When user clicks on download in playStore, google reads user's preferences (language, device, RAM) and installs only needed files.
// So AAB is most efficient