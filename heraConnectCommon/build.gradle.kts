plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")

}

android {
    namespace = "com.berbas.heraconnectcommon"
    compileSdk = 34

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "MONGODB_URI", "\"${findProperty("MONGODB_URI")}\"")

    }

    packaging {
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }

    packagingOptions {
        exclude("META-INF/LICENSE.md")
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

    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.jupiter.junit.jupiter)

    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // gson
    implementation("com.google.code.gson:gson:2.11.0")

    // room
    implementation("androidx.room:room-ktx:2.6.1")
    testImplementation(libs.testng)
    androidTestImplementation(libs.jupiter.junit.jupiter)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")

    // Testing
    testImplementation("org.robolectric:robolectric:4.6.1")

    // mockito
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.junit.jupiter.v3124)
    androidTestImplementation(libs.mockito.android)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter.v3124)
    testImplementation(libs.mockito.inline)
    // coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    //Junit
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.bson.kotlinx)
    implementation(libs.play.services.fitness)
    implementation(libs.play.services.auth)

}