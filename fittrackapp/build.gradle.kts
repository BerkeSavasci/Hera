plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.berbas.fittrackapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.berbas.fittrackapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.berbas.fittrackapp.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE-notice.md")
        resources.excludes.add ("META-INF/LICENSE.md")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // must be included to use edge to edge feature "KA WARUM!!!!!)
    implementation("androidx.activity:activity:1.9.0")

    // hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.play.services.basement)
    implementation(libs.core.ktx)
    testImplementation(libs.jupiter.junit.jupiter)
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")

    // room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)
    testImplementation("org.mockito:mockito-core:5.12.0")
    // For mocking final classes
    testImplementation ("org.mockito:mockito-inline:5.2.0")

    testImplementation(libs.kotlinx.coroutines.test)

    // roboelectric
    testImplementation ("org.robolectric:robolectric:4.8.2")

    // Mockk (for mocking dependencies)
    testImplementation("io.mockk:mockk:1.13.10")
    androidTestImplementation ("io.mockk:mockk-android:1.13.10")

    // Coroutines Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // AndroidX Test - Core Testing
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // coroutines test
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material:1.6.7")

    // testing compose
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.core.testing)


    // defaults
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx.v231)
    implementation(libs.appcompat)

    // default testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.truth)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // gson
    implementation("com.google.code.gson:gson:2.11.0")

    // local
    implementation(project(":heraConnectCommon"))
    wearApp(project(":wear"))

    // live data
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}