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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    // datastore preference
    implementation(libs.androidx.datastore.preferences)
    // encrypted shared preference
    implementation(libs.androidx.security.crypto)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}