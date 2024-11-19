plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services") // Google Services plugin
}

android {
    namespace = "com.example.drandile" // Namespace for R.java
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.drandile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true // Enable view binding
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)

    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation(libs.firebase.auth)
    implementation(libs.androidx.junit.ktx)

    testImplementation(libs.junit) // JUnit 4 for unit tests
    testImplementation("org.mockito:mockito-core:4.11.0") // Mockito core for mocking

    // Inline mock maker for final classes
    testImplementation("org.mockito:mockito-inline:4.11.0")

    // AndroidX Test for instrumented tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Latest JUnit version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso
    androidTestImplementation("androidx.test:core:1.5.0") // Core testing library
}
