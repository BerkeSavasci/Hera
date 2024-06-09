// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    //To configure the Hilt Gradle plugin with Gradle's new plugins DSL
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
buildscript{
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
    }
}