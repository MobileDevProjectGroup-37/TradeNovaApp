plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.traderapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.traderapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            type = "String",
            name = "COINCAP_API_KEY",
            value = "\"5b59f8cdeb486afbe3e45fe31e773e33a7ebad9bcc71f7e394241d51ad250fa9\""
        )
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // 📌 ViewModel с Coroutines
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.accompanist.systemuicontroller)
    implementation (libs.ui)
    // 🔥 **Firebase (Firestore + Auth + Storage)**
    implementation(platform("com.google.firebase:firebase-bom:32.7.2")) // 📌 Обнови BOM
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")

    // 🔑 **Google Sign-In**
    implementation(libs.googleid)

    // 🔄 **Navigation**
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)

    // 📌 **Hilt + DI**
    implementation("com.google.dagger:hilt-android:2.50")
    //implementation(libs.firebase.functions)
    kapt(libs.hilt.compiler) // 📌 Обязательно!
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // 🌍 **Coroutines**
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // 🌐 **Retrofit + WebSocket**
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // ✅ **Testing**
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
