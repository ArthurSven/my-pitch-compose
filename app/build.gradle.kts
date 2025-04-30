plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.devapps.mypitch"
    compileSdk = 34

    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/asmsiska/Desktop/keystores/mypitchkeystore/mypitch.jks")
            storePassword = (project.findProperty("KEYSTORE_PASSWORD") ?: "").toString()
            keyAlias = "pitchit"
            keyPassword = (project.findProperty("KEY_PASSWORD") ?: "").toString()
        }
    }
    defaultConfig {
        applicationId = "com.devapps.mypitch"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //viewmodel lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //koin dependency
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    //extra material icons
    implementation(libs.androidx.material.icons.extended)

    //navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.common.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.volley)
    implementation(libs.play.services.fido)
    implementation(libs.androidx.runner)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //retrofit
    implementation(libs.com.squareup.retrofit2.retrofit2)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //supabase
    implementation(libs.supabase.kt)
    implementation(libs.postgrest.kt)
    //ktor
    implementation(libs.ktor.client.android)

    //serializable
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.firebase.firestore.ktx)

    //google review api
    implementation(libs.review)
    implementation(libs.review.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}