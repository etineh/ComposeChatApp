buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
        maven { url = uri("https://www.jitpack.io") }
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}