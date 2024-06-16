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

    // mongodb
    implementation("org.mongodb:mongodb-driver-sync:4.4.0")
    implementation("org.litote.kmongo:kmongo-coroutine:4.4.0")

    // room
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")

    // Testing
    // mockito
    testImplementation(libs.mockito.inline)  // die funktionieren nicht aber ich lass die drin
    testImplementation(libs.mockito.junit.jupiter) // die funktionieren nicht aber ich lass die drin
    testImplementation(libs.mockk)
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-inline:3.12.4")
    testImplementation("org.mockito:mockito-junit-jupiter:3.12.4")
    // coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    //Junit
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.bson.kotlinx)
    implementation(libs.play.services.fitness)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.connect.client)

}